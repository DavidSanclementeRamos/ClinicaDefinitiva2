package com.example.ClinicaDefinitiva.application.shared.service;

import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.event.AuthorizationAuditEvent;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuditLogger;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Implementación ABAC de AuthorizationHelper.
 *
 * RESPONSABILIDAD: validar políticas contextuales (ABAC).
 *   - SectorBasedPolicy:    ¿Este RECEPTIONIST está en el sector correcto?
 *   - OwnershipPolicy:      ¿Este PATIENT/GUARDIAN accede solo a sus propios datos?
 *   - SpecialtyBasedPolicy: ¿Este DENTIST tiene la especialidad del servicio?
 *
 * NO evalúa RBAC base — eso ya lo hizo @RequiresPermission (AOP).
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * CUÁNDO CARGAR EL RECEPTIONIST
 * ─────────────────────────────────────────────────────────────────────────────
 * La decisión se delega a authorizationService.requiresSectorContext(), que a su
 * vez consulta SectorBasedPolicy.appliesTo(). De esta forma la única fuente de
 * verdad sobre "qué operaciones necesitan sector" es SectorBasedPolicy.SECTOR_REQUIREMENTS.
 *
 * Añadir una nueva operación gateada por sector solo requiere agregar una entrada
 * al mapa de SectorBasedPolicy. Este helper se actualiza automáticamente.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * IMPORTANTE: sector como nombre del enum, no como descripción
 * ─────────────────────────────────────────────────────────────────────────────
 * El SecurityContext almacena el sector usando Sector.toString() que devuelve
 * el nombre del enum ("HUMAN_RESOURCES", "BILLING"…).
 * SectorBasedPolicy lo parsea con Sector.fromString() → Type.valueOf().
 * Usar getDescription() ("Recursos Humanos") causaría que el parse fallara
 * silenciosamente y la política denegara toda operación de sector.
 */
@Service
public class DefaultAuthorizationHelper implements AuthorizationHelper {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAuthorizationHelper.class);

    private final ReceptionRepository receptionRepository;
    private final AuthorizationService authorizationService;
    private final AuditLogger auditLogger;

    public DefaultAuthorizationHelper(
            ReceptionRepository receptionRepository,
            AuthorizationService authorizationService,
            AuditLogger auditLogger) {
        this.receptionRepository = receptionRepository;
        this.authorizationService = authorizationService;
        this.auditLogger = auditLogger;
    }

    @Override
    public void authorize(
            UserIdentityId requesterId,
            RolId requesterRolId,
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action,
            AuthorizationContext authContext) {

        long startTime = System.nanoTime();

        try {
            // 1. Cargar receptionist solo si SectorBasedPolicy aplica a esta operación
            Receptionist receptionist = null;
            if (authorizationService.requiresSectorContext(resource, action)) {
                receptionist = getReceptionistOrThrow(requesterId);
            }

            // 2. Construir SecurityContext con atributos ABAC
            SecurityContext securityContext = buildSecurityContext(
                    requesterId, resource, action, receptionist, authContext
            );

            // 3. Validar políticas ABAC (RBAC ya fue validado por @RequiresPermission)
            boolean authorized = authorizationService.isAuthorizedByContext(requesterRolId, securityContext);

            // 4. Auditar decisión
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            auditAuthorizationDecision(requesterId, requesterRolId, resource, action,
                    authorized, durationMs, authContext);

            // 5. Lanzar si denegado
            if (!authorized) {
                throw new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                        VOContext.AUTHORIZATION
                );
            }

        } catch (BusinessRuleViolationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during ABAC authorization check", e);
            auditLogger.logAuthorizationError(
                    requesterId.value(), resource.name(), action.name(), e
            );
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_SYSTEM_ERROR,
                    VOContext.AUTHORIZATION
            );
        }
    }

    @Override
    public boolean checkAuthorization(
            UserIdentityId requesterId,
            RolId requesterRolId,
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action,
            AuthorizationContext authContext) {
        try {
            authorize(requesterId, requesterRolId, resource, action, authContext);
            return true;
        } catch (BusinessRuleViolationException e) {
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MÉTODOS PRIVADOS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Obtiene el receptionist asociado al requesterId o lanza excepción de negocio.
     * Solo se llama cuando requiresSectorContext() == true.
     */
    private Receptionist getReceptionistOrThrow(UserIdentityId requesterId) {
        return receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
    }

    /**
     * Construye SecurityContext mapeando AuthorizationContext (capa de aplicación)
     * a SecurityContext (capa de dominio).
     *
     * El sector se almacena usando Sector.toString() (nombre del enum: "HUMAN_RESOURCES")
     * porque SectorBasedPolicy lo parsea con Type.valueOf(). Usar getDescription()
     * produciría "Recursos Humanos" que Type.valueOf() no reconoce.
     */
    private SecurityContext buildSecurityContext(
            UserIdentityId requesterId,
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action,
            Receptionist receptionist,
            AuthorizationContext authContext) {

        Permission permission = Permission.of(
                ResourceCatalog.of(resource),
                ActionCatalog.of(action)
        );

        SecurityContext.Builder builder = SecurityContext.builder(permission, requesterId);

        // Sector: toString() devuelve el nombre del enum ("HUMAN_RESOURCES"),
        // NO getDescription() que devuelve la descripción en español ("Recursos Humanos")
        if (receptionist != null) {
            builder.withSector(receptionist.getSector().toString());
        }

        // Mapear atributos del AuthorizationContext
        if (authContext != null) {
            if (authContext.getResourceId() != null) {
                builder.withResourceId(authContext.getResourceId());
            }
            if (authContext.getResourceOwnerId() != null) {
                builder.withResourceOwnerId(authContext.getResourceOwnerId());
            }
            if (authContext.getPatientGuardianId() != null) {
                builder.withPatientGuardianId(authContext.getPatientGuardianId());
            }
            if (authContext.getAssignedDentistUserId() != null) {
                builder.withAttribute("assignedDentistUserId", authContext.getAssignedDentistUserId());
            }
            if (authContext.getDentistSpecialties() != null) {
                builder.withDentistSpecialties(authContext.getDentistSpecialties());
            }
            if (authContext.getAdditionalAttributes() != null) {
                authContext.getAdditionalAttributes().forEach(builder::withAttribute);
            }
        }

        return builder.build();
    }

    private void auditAuthorizationDecision(
            UserIdentityId requesterId,
            RolId rolId,
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action,
            boolean authorized,
            long durationMs,
            AuthorizationContext authContext) {

        AuthorizationAuditEvent.Builder eventBuilder = AuthorizationAuditEvent.builder()
                .userId(requesterId)
                .rolId(rolId.getValue())
                .resource(resource)
                .action(action)
                .decision(authorized ?
                        AuthorizationAuditEvent.Decision.ALLOW :
                        AuthorizationAuditEvent.Decision.DENY)
                .timestamp(Instant.now())
                .durationMs(durationMs);

        if (authContext != null) {
            if (authContext.getResourceId() != null) {
                eventBuilder.contextAttribute("resourceId", authContext.getResourceId());
            }
            if (authContext.getResourceOwnerId() != null) {
                eventBuilder.contextAttribute("resourceOwnerId", authContext.getResourceOwnerId().value());
            }
            if (authContext.getPatientGuardianId() != null) {
                eventBuilder.contextAttribute("guardianId", authContext.getPatientGuardianId());
            }
        }

        if (!authorized) {
            eventBuilder.denyReason("ABAC policy evaluation failed");
        }

        auditLogger.logAuthorizationDecision(eventBuilder.build());
    }
}