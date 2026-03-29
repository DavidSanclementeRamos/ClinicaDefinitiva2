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
 *   - SectorBasedPolicy: ¿Este RECEPTIONIST está en el sector correcto?
 *   - OwnershipPolicy:   ¿Este PATIENT accede solo a sus propios datos?
 *   - SpecialtyBasedPolicy: ¿Este DENTIST tiene la especialidad del servicio?
 *
 * NO hace verificación RBAC base — eso ya lo hizo @RequiresPermission (AOP).
 *
 * CUÁNDO CARGAR EL RECEPTIONIST:
 * Solo cuando la operación específica lo requiere según SectorBasedPolicy:
 *   → DELETE sobre DENTIST
 * Para CUALQUIER otro recurso o acción, NO se busca el receptionist.
 *
 * Por qué el diseño anterior estaba mal:
 * El código anterior cargaba el receptionist para TODO recurso que no fuera
 * PATIENT o GUARDIAN. Eso significaba que un ADMINISTRATOR intentando leer
 * un ROLE fallaba porque no era receptionist. El error era confundir
 * "quién es el usuario" con "qué tipo de empleado es".
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
            // 1. Cargar receptionist SOLO si la operación específica lo requiere
            Receptionist receptionist = null;
            if (requiresSectorValidation(resource, action)) {
                receptionist = getReceptionistOrThrow(requesterId);
            }

            // 2. Construir SecurityContext con atributos ABAC
            SecurityContext securityContext = buildSecurityContext(
                    requesterId, resource, action, receptionist, authContext
            );

            // 3. Validar solo políticas ABAC (RBAC ya fue validado por @RequiresPermission)
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
     * Determina si la operación específica requiere cargar datos del receptionist
     * para validar sector.
     *
     * Regla: solo DELETE sobre DENTIST (única operación donde SectorBasedPolicy.appliesTo() = true).
     * Para CUALQUIER otro recurso/acción, no se necesita el receptionist.
     *
     * Esto está alineado con SectorBasedPolicy.appliesTo():
     *   ActionCatalog.DELETE + ResourceCatalog.DENTIST → true
     *   todo lo demás → false
     */
    private boolean requiresSectorValidation(
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action) {
        return resource == ResourceCatalog.BasicResource.DENTIST
                && action == ActionCatalog.BasicAction.DELETE;
    }

    /**
     * Obtiene el receptionist o lanza excepción de negocio.
     * Solo se llama cuando requiresSectorValidation() == true.
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

        // Agregar sector solo si se cargó el receptionist
        if (receptionist != null) {
            builder.withSector(receptionist.getSector().getDescription());
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