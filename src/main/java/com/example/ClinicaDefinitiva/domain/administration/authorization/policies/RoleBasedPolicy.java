package com.example.ClinicaDefinitiva.domain.administration.authorization.policies;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Política RBAC centralizada — define permisos base por rol.
 *
 * RESPONSABILIDAD: responder "¿puede el rol X hacer la acción Y sobre el recurso Z?"
 * sin conocer contexto (sector, ownership, especialidad).
 *
 * Esta es la ÚNICA clase que debes modificar para cambios RBAC.
 *
 * LEYENDA DE LA MATRIZ
 * ─────────────────────────────────────────────────────────────────────────────
 *  ✓       = permitido sin restricciones adicionales
 *  ✓(RRHH) = permitido solo si sector == HUMAN_RESOURCES   → SectorBasedPolicy
 *  ✓(BILL) = permitido solo si sector == BILLING           → SectorBasedPolicy
 *  ✓(ADMI) = permitido solo si sector == ADMINISTRATION    → SectorBasedPolicy
 *  ✓(MEDR) = permitido solo si sector == MEDICAL_RECORDS   → SectorBasedPolicy
 *  ✓(OWN)  = permitido solo si el recurso pertenece al actor → OwnershipPolicy
 *  ✓(ESP)  = permitido solo para servicios de su especialidad → SpecialtyBasedPolicy
 *  -       = denegado sin excepción (no en catálogo de este rol)
 *
 * ┌──────────────────────────┬─────────────┬──────────────────────────────────┬──────────┬──────────┬────────────┐
 * │ Recurso                  │ ADMINISTRATOR│         RECEPTIONIST             │  DENTIST │  PATIENT │  GUARDIAN  │
 * │                          │             │ (sector governs sensitive ops)   │          │          │            │
 * ├──────────────────────────┼─────────────┼──────────────────────────────────┼──────────┼──────────┼────────────┤
 * │ ROLE                     │ CRUD+admin  │               -                  │    -     │    -     │     -      │
 * │ ASSIGNMENT               │ CRUD+ops    │               -                  │    -     │    -     │     -      │
 * │ USER_IDENTITY            │ CRUD+ops    │               -                  │    -     │    -     │     -      │
 * ├──────────────────────────┼─────────────┼──────────────────────────────────┼──────────┼──────────┼────────────┤
 * │ PATIENT                  │ R           │ C ✓ / R ✓ / U ✓(sector*)        │ R ✓(ESP) │ RU ✓(OWN)│ RU ✓(OWN) │
 * │ DENTIST                  │ R           │ C ✓(RRHH) / R ✓ / U ✓(RRHH)    │    -     │    -     │     -      │
 * │                          │             │ D ✓(RRHH) / SUSPEND ✓(RRHH)     │          │          │            │
 * │ GUARDIAN                 │ R           │ CRU ✓                            │    -     │    -     │ RU ✓(OWN)  │
 * │ RECEPTIONIST             │ R           │ R ✓ / C ✓(RRHH) / U ✓(RRHH)    │    -     │    -     │     -      │
 * │                          │             │ D ✓(RRHH) / SUSPEND ✓(RRHH)     │          │          │            │
 * ├──────────────────────────┼─────────────┼──────────────────────────────────┼──────────┼──────────┼────────────┤
 * │ APPOINTMENT              │ R           │ CRUD + CANCEL/RESCHEDULE/SCHED ✓ │ R+COMPL  │ R ✓      │ CR+CANCEL ✓│
 * │ AVAILABILITY             │ R           │               -                  │ CRUD ✓   │    -     │     -      │
 * │ SHIFT                    │ R           │ R ✓ / C ✓(RRHH) / U ✓(RRHH)    │ RU ✓     │    -     │     -      │
 * │                          │             │ D ✓(RRHH)                        │          │          │            │
 * ├──────────────────────────┼─────────────┼──────────────────────────────────┼──────────┼──────────┼────────────┤
 * │ PROVIDED_SERVICE         │ R           │ R ✓ / U ✓(ADMI)                 │ CRU ✓(ESP)│ R ✓     │ R ✓        │
 * ├──────────────────────────┼─────────────┼──────────────────────────────────┼──────────┼──────────┼────────────┤
 * │ INVOICE                  │ CRUD+ops    │ R ✓ / C ✓(BILL) / U ✓(BILL)     │ R ✓      │ R ✓      │ R ✓        │
 * │                          │             │ D ✓(BILL) / APPROVE ✓(BILL)      │          │          │            │
 * │                          │             │ REVERSE ✓(BILL) / POST ✓(BILL)   │          │          │            │
 * │ PAYMENT                  │ CRUD        │ R ✓ / C ✓(BILL) / U ✓(BILL)     │ R ✓      │ R ✓      │ R ✓        │
 * │                          │             │ D ✓(BILL)                        │          │          │            │
 * │ RATE                     │ CRUD        │ R ✓                              │ R ✓      │    -     │     -      │
 * │ CONTRACT                 │ CRUD        │ R ✓ / C ✓(ADMI) / U ✓(ADMI)     │    -     │    -     │     -      │
 * │                          │             │ D ✓(ADMI)                        │          │          │            │
 * │ JOURNAL_ENTRY            │ CRUD+ops    │               -                  │    -     │    -     │     -      │
 * ├──────────────────────────┼─────────────┼──────────────────────────────────┼──────────┼──────────┼────────────┤
 * │ COMPANY                  │ CRUD        │ R ✓ / U ✓(ADMI)                 │    -     │    -     │     -      │
 * │ ADMINISTRATIVE_REPORT    │ CRUD        │ R ✓ / C ✓(ADMI) / U ✓(ADMI)     │ R ✓      │    -     │     -      │
 * │                          │             │ D ✓(ADMI)                        │          │          │            │
 * └──────────────────────────┴─────────────┴──────────────────────────────────┴──────────┴──────────┴────────────┘
 *
 * (*) PATIENT UPDATE para RECEPTIONIST:
 *   - datos básicos  (sensitiveData=false) → sector RECEPTION / CUSTOMER_SERVICE / CALL_CENTER
 *   - datos sensibles (sensitiveData=true) → sector MEDICAL_RECORDS
 *   Ambos casos gestionados por SectorBasedPolicy.
 */
public class RoleBasedPolicy implements PermissionPolicy {

    private static final Map<RolEnum, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        configureAdministratorPermissions();
        configureReceptionistPermissions();
        configureDentistPermissions();
        configurePatientPermissions();
        configureGuardianPermissions();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADMINISTRATOR: gestiona usuarios, roles y asignaciones; puede ver todo
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureAdministratorPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // ── Seguridad / gestión de acceso ──────────────────────────────────
        // Roles
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CREATE_CUSTOM)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CLONE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.ADD)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.REMOVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.SET_PERMISSIONS)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CHECK)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.ACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.DEACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.SUSPEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.MARK_DELETED)));

        // Asignaciones
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.CREATE_TEMPORARY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.CREATE_PERMANENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.REVOKE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.REVOKE_ALL)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.EXTEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.IS_ACTIVE_AT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.UPDATE_PRIMARY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.DELETE)));

        // Identidades de usuario
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.DEACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.SUSPEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.ACTIVATE)));

        // ── Actores (solo lectura; los crea el flujo de registro) ──────────
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.READ)));

        // ── Facturación y contabilidad (gestión completa) ──────────────────
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.APPROVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.POST)));

        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.DELETE)));

        // RATE
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.DELETE)));

        // CONTRACT
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.DELETE)));

        // JOURNAL_ENTRY
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.POST)));

        // COMPANY
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.DELETE)));

        // ADMINISTRATIVE_REPORT
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.DELETE)));

        // ── Agenda (solo lectura; la opera RECEPTIONIST) ───────────────────
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.ADMINISTRATOR, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RECEPTIONIST: opera agenda, gestiona actores del sector, facturación básica
    // ─────────────────────────────────────────────────────────────────────────
    

    /**
     * RECEPTIONIST: rol polivalente de clínica dental.
     *
     * Sus permisos base cubre todas las funciones operativas posibles.
     * Las operaciones sensibles están gateadas por SectorBasedPolicy:
     *
     *   HUMAN_RESOURCES → gestión de personal (dentistas, recepcionistas, turnos)
     *   BILLING         → gestión financiera  (facturas, pagos)
     *   ADMINISTRATION  → configuración       (empresa, contratos, servicios, reportes)
     *   MEDICAL_RECORDS → datos clínicos      (actualización sensible de pacientes)
     *
     * Un recepcionista de Recepción solo puede hacer lo marcado como ✓ sin gate.
     * Un recepcionista de RRHH puede hacer lo anterior + ops marcadas ✓(RRHH).
     * Y así sucesivamente.
     */
    private static void configureReceptionistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // ── PATIENT ────────────────────────────────────────────────────────
        // CREATE: registro de nuevos pacientes (datos básicos) — sin gate
        // READ:   consulta general — sin gate
        // UPDATE: SectorBasedPolicy distingue datos básicos (RECEPTION/CS/CC)
        //         vs. datos sensibles (MEDICAL_RECORDS) via atributo "sensitiveData"
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.UPDATE)));

        // ── GUARDIAN ───────────────────────────────────────────────────────
        // Registro y gestión de tutores — sin gate
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.UPDATE)));

        // ── DENTIST ────────────────────────────────────────────────────────
        // READ: cualquier recepcionista necesita ver dentistas para agendar citas
        // CREATE/UPDATE/DELETE/SUSPEND: exclusivo de RRHH → SectorBasedPolicy
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.SUSPEND)));

        // ── RECEPTIONIST ───────────────────────────────────────────────────
        // READ: consulta de compañeros — sin gate
        // CREATE/UPDATE/DELETE/SUSPEND: gestión de personal → SectorBasedPolicy (RRHH)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.SUSPEND)));

        // ── APPOINTMENT ────────────────────────────────────────────────────
        // Gestión completa de citas — función central de cualquier recepcionista
        // Sin gate: la agenda es responsabilidad de todos en front-desk
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CANCEL)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.RESCHEDULE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.SCHEDULE)));

        // ── SHIFT ──────────────────────────────────────────────────────────
        // READ: cualquier recepcionista consulta turnos para coordinar agenda
        // CREATE/UPDATE/DELETE: gestión de turnos de personal → SectorBasedPolicy (RRHH)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.DELETE)));

        // ── PROVIDED_SERVICE ───────────────────────────────────────────────
        // READ: cualquier recepcionista informa a pacientes sobre los servicios
        // UPDATE: solo admin puede cambiar datos/precios del catálogo → SectorBasedPolicy (ADMI)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.UPDATE)));

        // ── INVOICE ────────────────────────────────────────────────────────
        // READ: cualquier recepcionista puede consultar facturas para informar al paciente
        // Mutaciones: solo Facturación → SectorBasedPolicy (BILLING)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.APPROVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.POST)));

        // ── PAYMENT ────────────────────────────────────────────────────────
        // READ: cualquier recepcionista puede verificar si un pago está registrado
        // Mutaciones: solo Facturación → SectorBasedPolicy (BILLING)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.DELETE)));

        // ── RATE ───────────────────────────────────────────────────────────
        // Solo lectura: cualquier recepcionista necesita conocer tarifas para cotizar
        // CREATE/UPDATE/DELETE de RATE es exclusivo de ADMINISTRATOR (no en este rol)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.READ)));

        // ── CONTRACT ───────────────────────────────────────────────────────
        // READ: consulta general de contratos — sin gate
        // Mutaciones: exclusivo de Administración → SectorBasedPolicy (ADMINISTRATION)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.DELETE)));

        // ── COMPANY ────────────────────────────────────────────────────────
        // READ: cualquier recepcionista conoce los datos básicos de la empresa
        // UPDATE: solo Administración → SectorBasedPolicy (ADMINISTRATION)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.UPDATE)));

        // ── ADMINISTRATIVE_REPORT ──────────────────────────────────────────
        // READ: cualquier recepcionista puede consultar reportes
        // Mutaciones: exclusivo de Administración → SectorBasedPolicy (ADMINISTRATION)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.DELETE)));

        ROLE_PERMISSIONS.put(RolEnum.RECEPTIONIST, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DENTIST: gestiona sus pacientes asignados, disponibilidad y servicios
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureDentistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Lectura de pacientes asignados (ABAC: SpecialtyBasedPolicy filtrará por asignación)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));

        // Servicios clínicos propios (ABAC: SpecialtyBasedPolicy valida especialidad)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.UPDATE)));

        // Gestión de citas y disponibilidad propia
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.COMPLETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.MARK_AS_NO_SHOW)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.DELETE)));

        // Turnos propios
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.UPDATE)));

        // Consulta de facturación
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.READ)));

        // Reportes
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.DENTIST, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PATIENT: auto-gestión con restricciones ABAC de ownership
    // ─────────────────────────────────────────────────────────────────────────
    private static void configurePatientPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Auto-gestión (ABAC: OwnershipPolicy valida que sea su propio registro)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.UPDATE)));

        // Citas propias
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));

        // Consulta de servicios y facturación propios
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.PATIENT, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GUARDIAN: gestión de datos propios y de pacientes bajo tutela
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureGuardianPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Datos propios (ABAC: OwnershipPolicy)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.UPDATE)));

        // Pacientes bajo tutela (ABAC: OwnershipPolicy via guardianship)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.UPDATE)));

        // Citas de tutelados
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CANCEL)));

        // Consulta de servicios y facturación
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.GUARDIAN, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────────

    public static Set<Permission> getPermissionsFor(RolEnum rolEnum) {
        return ROLE_PERMISSIONS.getOrDefault(rolEnum, Set.of());
    }

    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        Set<Permission> permissions = ROLE_PERMISSIONS.get(rol.getRolEnum());
        if (permissions == null) return false;
        return permissions.contains(context.getPermission());
    }

    @Override
    public int getPriority() {
        return 100;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers privados para reducir verbosidad
    // ─────────────────────────────────────────────────────────────────────────
    private static ResourceCatalog r(ResourceCatalog.BasicResource resource) {
        return ResourceCatalog.of(resource);
    }

    private static ActionCatalog a(ActionCatalog.BasicAction action) {
        return ActionCatalog.of(action);
    }
}