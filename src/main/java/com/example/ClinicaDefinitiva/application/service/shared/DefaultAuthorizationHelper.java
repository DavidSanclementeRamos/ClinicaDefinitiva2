package com.example.ClinicaDefinitiva.application.service.shared;

import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.event.AuthorizationAuditEvent;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuditLogger;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

/**
 * Implementación por defecto de AuthorizationHelper.
 * 
 * RESPONSABILIDADES:
 * 1. Obtener receptionist para validación de sector (cuando aplica)
 * 2. Construir SecurityContext con todos los atributos ABAC necesarios
 * 3. Delegar validación al AuthorizationService (PolicyEngine)
 * 4. Auditar TODAS las decisiones (permitidas y denegadas)
 * 5. Lanzar excepción consistente si denegado
 * 6. Manejar errores inesperados de forma segura
 * 
 * POLÍTICAS QUE MANEJA:
 * - SectorBasedPolicy: Requiere receptionist con sector válido
 * - OwnershipPolicy: Valida resourceOwnerId del contexto
 * - GuardianshipPolicy: Valida patientGuardianId del contexto
 * - SpecialtyBasedPolicy: Valida dentistSpecialties del contexto
 * - RoleBasedPolicy: Siempre evaluada por el PolicyEngine
 */
@Service
public class DefaultAuthorizationHelper implements AuthorizationHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultAuthorizationHelper.class);
    
    // Recursos que NO requieren sector (ownership-based)
    private static final Set<ResourceCatalog.BasicResource> OWNERSHIP_RESOURCES = Set.of(
        ResourceCatalog.BasicResource.PATIENT,
        ResourceCatalog.BasicResource.GUARDIAN
    );
    
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
            // 1. Obtener receptionist si el recurso requiere sector
            Receptionist receptionist = null;
            if (requiresSectorValidation(resource)) {
                receptionist = getReceptionistOrThrow(requesterId);
            }
            
            // 2. Construir SecurityContext con atributos ABAC
            SecurityContext securityContext = buildSecurityContext(
                requesterId,
                resource,
                action,
                receptionist,
                authContext
            );
            
            // 3. Validar autorización contra PolicyEngine
            boolean authorized = authorizationService.isAuthorized(requesterRolId, securityContext);
            
            // 4. Auditar decisión
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            auditAuthorizationDecision(
                requesterId,
                requesterRolId,
                resource,
                action,
                authorized,
                durationMs,
                authContext
            );
            
            // 5. Lanzar si denegado
            if (!authorized) {
                throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
                );
            }
            
        } catch (BusinessRuleViolationException e) {
            // Re-lanzar errores de negocio (incluye denegaciones)
            throw e;
        } catch (Exception e) {
            // Log y wrap excepciones inesperadas
            logger.error("Unexpected error during authorization check", e);
            auditLogger.logAuthorizationError(
                requesterId.value(),
                resource.name(),
                action.name(),
                e
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
    
    // ========== MÉTODOS PRIVADOS ==========
    
    /**
     * Determina si un recurso requiere validación de sector.
     * 
     * Recursos ownership-based (PATIENT, GUARDIAN) NO requieren sector
     * porque la autorización se basa en propiedad del recurso.
     */
    private boolean requiresSectorValidation(ResourceCatalog.BasicResource resource) {
        return !OWNERSHIP_RESOURCES.contains(resource);
    }
    
    /**
     * Obtiene receptionist o lanza excepción si no existe.
     * Solo se llama para recursos que requieren sector.
     */
    private Receptionist getReceptionistOrThrow(UserIdentityId requesterId) {
        return receptionRepository.findByUserId(requesterId)
            .orElseThrow(() -> new BusinessRuleViolationException(
                AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                VOContext.AUTHORIZATION
            ));
    }
    
    /**
     * Construye SecurityContext con todos los atributos ABAC necesarios.
     * 
     * Mapea AuthorizationContext (capa de aplicación) a SecurityContext (capa de dominio).
     */
    private SecurityContext buildSecurityContext(
            UserIdentityId requesterId,
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action,
            Receptionist receptionist,
            AuthorizationContext authContext) {
        
        // Construir Permission
        Permission permission = Permission.of(
            ResourceCatalog.of(resource),
            ActionCatalog.of(action)
        );
        
        // Builder de SecurityContext
        SecurityContext.Builder builder = SecurityContext
            .builder(permission, requesterId);
        
        // Agregar sector si receptionist existe
        if (receptionist != null) {
            builder.withSector(receptionist.getSector().getDescription());
        }
        
        // Agregar atributos del AuthorizationContext
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
                builder.withAttribute("assignedDentistUserId", 
                    authContext.getAssignedDentistUserId());
            }
            if (authContext.getDentistSpecialties() != null) {
                builder.withDentistSpecialties(authContext.getDentistSpecialties());
            }
            
            // Atributos adicionales genéricos
            if (authContext.getAdditionalAttributes() != null) {
                authContext.getAdditionalAttributes().forEach(builder::withAttribute);
            }
        }
        
        return builder.build();
    }
    
    /**
     * Audita la decisión de autorización.
     * 
     * Registra tanto decisiones permitidas como denegadas para:
     * - Compliance (SOC2, ISO 27001, GDPR)
     * - Debugging de permisos
     * - Detección de ataques
     * - Análisis de patrones de uso
     */
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
        
        // Agregar atributos de contexto relevantes para auditoría
        if (authContext != null) {
            if (authContext.getResourceId() != null) {
                eventBuilder.contextAttribute("resourceId", authContext.getResourceId());
            }
            if (authContext.getResourceOwnerId() != null) {
                eventBuilder.contextAttribute("resourceOwnerId", 
                    authContext.getResourceOwnerId().value());
            }
            if (authContext.getPatientGuardianId() != null) {
                eventBuilder.contextAttribute("guardianId", 
                    authContext.getPatientGuardianId());
            }
        }
        
        // Agregar razón si denegado
        if (!authorized) {
            eventBuilder.denyReason("Policy evaluation failed");
        }
        
        auditLogger.logAuthorizationDecision(eventBuilder.build());
    }
}
