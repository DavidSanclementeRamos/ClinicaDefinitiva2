package com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual;

import com.example.ClinicaDefinitiva.domain.actor.vo.Sector;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.PermissionPolicy;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;
import static com.example.ClinicaDefinitiva.domain.actor.vo.Sector.Type.*;

/**
 * Política de sector — restringe operaciones sensibles al sector correcto.
 *
 * APLICA SOLO A: RECEPTIONIST
 * OTROS ROLES: siempre pasan (sus restricciones son RBAC, OwnershipPolicy o SpecialtyBasedPolicy)
 *
 * Patrón de evaluación:
 *   1. RoleBasedPolicy verifica si el permiso existe para el rol          ← GATE 1
 *   2. SectorBasedPolicy verifica si el sector del actor es el correcto   ← GATE 2
 *
 * El mapa central SECTOR_REQUIREMENTS define la regla:
 *   clave "RESOURCE:ACTION" → Sector.Type requerido
 *
 * Caso especial PATIENT:UPDATE:
 *   El ApplicationService distingue datos básicos vs. sensibles pasando
 *   el atributo "sensitiveData" (Boolean) en el AuthorizationContext.
 *
 * ─────────────────────────────────────────────────────────────────────────
 * MAPEO COMPLETO
 * ─────────────────────────────────────────────────────────────────────────
 *
 * HUMAN_RESOURCES → gestión de personal
 *   DENTIST:     CREATE, UPDATE, DELETE, SUSPEND
 *   RECEPTIONIST: CREATE, UPDATE, DELETE, SUSPEND
 *   SHIFT:       CREATE, UPDATE, DELETE
 *
 * BILLING → gestión financiera
 *   INVOICE: CREATE, UPDATE, DELETE, APPROVE, REVERSE, POST
 *   PAYMENT: CREATE, UPDATE, DELETE
 *
 * ADMINISTRATION → configuración y contratos
 *   COMPANY:               UPDATE
 *   CONTRACT:              CREATE, UPDATE, DELETE
 *   PROVIDED_SERVICE:      UPDATE
 *   ADMINISTRATIVE_REPORT: CREATE, UPDATE, DELETE
 *
 * MEDICAL_RECORDS → datos clínicos sensibles (caso especial vía sensitiveData)
 *   PATIENT:UPDATE con sensitiveData=true → requiere MEDICAL_RECORDS
 *   PATIENT:UPDATE con sensitiveData=false → permite RECEPTION / CUSTOMER_SERVICE / CALL_CENTER
 *
 * SIN RESTRICCIÓN DE SECTOR (cualquier RECEPTIONIST):
 *   PATIENT CREATE/READ, GUARDIAN CRU, APPOINTMENT CRUD+ops,
 *   DENTIST READ, RECEPTIONIST READ, INVOICE READ, PAYMENT READ,
 *   RATE READ, CONTRACT READ, COMPANY READ, PROVIDED_SERVICE READ,
 *   ADMINISTRATIVE_REPORT READ
 */
public class SectorBasedPolicy implements PermissionPolicy {

    /**
     * Mapa central de restricciones de sector.
     *
     * Clave: "RESOURCE_CODE:ACTION_CODE"
     * Valor: Sector.Type requerido para que un RECEPTIONIST pueda ejecutar la operación.
     *
     * Las operaciones que NO aparecen aquí son libres de sector para RECEPTIONIST.
     */
    private static final Map<String, Sector.Type> SECTOR_REQUIREMENTS = Map.ofEntries(

        // ── HUMAN_RESOURCES: gestión de personal ──────────────────────────
        entry("DENTIST:CREATE",        HUMAN_RESOURCES),
        entry("DENTIST:UPDATE",        HUMAN_RESOURCES),
        entry("DENTIST:DELETE",        HUMAN_RESOURCES),
        entry("DENTIST:SUSPEND",       HUMAN_RESOURCES),
        entry("RECEPTIONIST:CREATE",   HUMAN_RESOURCES),
        entry("RECEPTIONIST:UPDATE",   HUMAN_RESOURCES),
        entry("RECEPTIONIST:DELETE",   HUMAN_RESOURCES),
        entry("RECEPTIONIST:SUSPEND",  HUMAN_RESOURCES),
        entry("SHIFT:CREATE",          HUMAN_RESOURCES),
        entry("SHIFT:UPDATE",          HUMAN_RESOURCES),
        entry("SHIFT:DELETE",          HUMAN_RESOURCES),

        // ── BILLING: gestión financiera ────────────────────────────────────
        entry("INVOICE:CREATE",        BILLING),
        entry("INVOICE:UPDATE",        BILLING),
        entry("INVOICE:DELETE",        BILLING),
        entry("INVOICE:APPROVE",       BILLING),
        entry("INVOICE:REVERSE",       BILLING),
        entry("INVOICE:POST",          BILLING),
        entry("PAYMENT:CREATE",        BILLING),
        entry("PAYMENT:UPDATE",        BILLING),
        entry("PAYMENT:DELETE",        BILLING),

        // ── ADMINISTRATION: configuración y contratos ──────────────────────
        entry("COMPANY:UPDATE",                    ADMINISTRATION),
        entry("CONTRACT:CREATE",                   ADMINISTRATION),
        entry("CONTRACT:UPDATE",                   ADMINISTRATION),
        entry("CONTRACT:DELETE",                   ADMINISTRATION),
        entry("PROVIDED_SERVICE:UPDATE",           ADMINISTRATION),
        entry("ADMINISTRATIVE_REPORT:CREATE",      ADMINISTRATION),
        entry("ADMINISTRATIVE_REPORT:UPDATE",      ADMINISTRATION),
        entry("ADMINISTRATIVE_REPORT:DELETE",      ADMINISTRATION)
    );

    /**
     * Sectores autorizados para actualizar datos BÁSICOS de un paciente.
     * (nombre, teléfono, dirección — no datos clínicos)
     */
    private static final Set<Sector.Type> BASIC_PATIENT_UPDATE_SECTORS = Set.of(
        RECEPTION,
        CUSTOMER_SERVICE,
        CALL_CENTER
    );

    // ─────────────────────────────────────────────────────────────────────────
    // PermissionPolicy API
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean appliesTo(SecurityContext context) {
        String key = buildKey(context);
        // Aplica si la operación está en el mapa O es el caso especial PATIENT:UPDATE
        return SECTOR_REQUIREMENTS.containsKey(key) || "PATIENT:UPDATE".equals(key);
    }

    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        // Solo restringe RECEPTIONIST; los demás roles tienen sus propias políticas
        if (rol.getRolEnum() != RolEnum.RECEPTIONIST) {
            return true;
        }

        String key = buildKey(context);
        String rawSector = context.getAttribute("sector", String.class).orElse(null);

        // Sin sector declarado en el contexto → denegar cualquier operación gateada
        if (rawSector == null || rawSector.isBlank()) {
            return false;
        }

        Sector.Type userSector;
        try {
            userSector = Sector.fromString(rawSector).getValue();
        } catch (Exception e) {
            // Sector inválido (no existe en el enum) → denegar
            return false;
        }

        // Caso especial: distingue datos básicos vs. sensibles en PATIENT
        if ("PATIENT:UPDATE".equals(key)) {
            return isPatientUpdateAllowed(userSector, context);
        }

        // Caso general: verificar sector requerido en el mapa
        Sector.Type requiredSector = SECTOR_REQUIREMENTS.get(key);
        if (requiredSector == null) {
            // Operación no listada → sin restricción de sector para este rol
            return true;
        }

        return userSector == requiredSector;
    }

    @Override
    public int getPriority() {
        return 200; // Después de RoleBasedPolicy (100), antes de OwnershipPolicy (300)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Lógica especial
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Valida PATIENT:UPDATE según el tipo de datos involucrados.
     *
     * El ApplicationService que invoque una actualización de datos sensibles DEBE
     * incluir en el AuthorizationContext:
     *
     *   AuthorizationContext.builder()
     *       .withAttribute("sensitiveData", true)
     *       .build()
     *
     * Si el atributo está ausente o es false, se interpreta como datos básicos.
     *
     * Datos BÁSICOS (nombre, contacto, dirección):
     *   → permitido para RECEPTION, CUSTOMER_SERVICE, CALL_CENTER
     *
     * Datos SENSIBLES (alergias, medicamentos, historial):
     *   → permitido solo para MEDICAL_RECORDS
     */
    private boolean isPatientUpdateAllowed(Sector.Type userSector, SecurityContext context) {
        boolean sensitiveData = context
                .getAttribute("sensitiveData", Boolean.class)
                .orElse(false);

        if (sensitiveData) {
            return userSector == MEDICAL_RECORDS;
        }

        return BASIC_PATIENT_UPDATE_SECTORS.contains(userSector);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Construye la clave de lookup: "RESOURCE_CODE:ACTION_CODE"
     * Ejemplo: "INVOICE:CREATE", "DENTIST:DELETE"
     */
    private String buildKey(SecurityContext context) {
        String resource = context.getPermission().getResource().getCode();
        String action   = context.getPermission().getAction().getCode();
        return resource + ":" + action;
    }
}