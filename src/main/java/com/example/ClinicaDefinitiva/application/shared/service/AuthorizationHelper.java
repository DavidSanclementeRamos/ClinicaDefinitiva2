package com.example.ClinicaDefinitiva.application.shared.service;

import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

/**
 * Helper centralizado para autorización en ApplicationServices.
 * 
 * PROPÓSITO:
 * - Eliminar duplicación de código de autorización en services
 * - Garantizar consistencia en validaciones de permisos
 * - Centralizar auditoría de decisiones
 * - Facilitar testing y mantenimiento
 * 
 * USO BÁSICO:
 * authorizationHelper.authorize(
 *     requesterId,
 *     requesterRolId,
 *     ResourceCatalog.BasicResource.COMPANY,
 *     ActionCatalog.BasicAction.CREATE,
 *     AuthorizationContext.builder().build() // sin atributos adicionales
 * );
 * 
 * USO CON OWNERSHIP:
 * authorizationHelper.authorize(
 *     requesterId,
 *     requesterRolId,
 *     ResourceCatalog.BasicResource.PATIENT,
 *     ActionCatalog.BasicAction.UPDATE,
 *     AuthorizationContext.builder()
 *         .withResourceId(patientId)
 *         .withOwnership(patientUserId)
 *         .build()
 * );
 * 
 * USO CON GUARDIANSHIP:
 * authorizationHelper.authorize(
 *     requesterId,
 *     requesterRolId,
 *     ResourceCatalog.BasicResource.PATIENT,
 *     ActionCatalog.BasicAction.READ,
 *     AuthorizationContext.builder()
 *         .withResourceId(patientId)
 *         .withPatientGuardianId(guardianId)
 *         .build()
 * );
 * 
 * POLÍTICAS SOPORTADAS:
 * - RoleBasedPolicy (RBAC base)
 * - SectorBasedPolicy (receptionist por sector)
 * - OwnershipPolicy (usuario solo accede a sus recursos)
 * - GuardianshipPolicy (tutor accede a recursos de pacientes)
 * - SpecialtyBasedPolicy (dentista solo ve servicios de su especialidad)
 * - AssignmentPolicy (dentista solo ve tratamientos asignados)
 */
public interface AuthorizationHelper {
    
    /**
     * Valida autorización para una operación.
     * 
     * FLUJO:
     * 1. Obtiene Receptionist (si aplica para sector)
     * 2. Construye SecurityContext con atributos ABAC
     * 3. Valida contra PolicyEngine
     * 4. Audita decisión (permitida o denegada)
     * 5. Lanza BusinessRuleViolationException si denegado
     * 
     * @param requesterId Usuario que solicita la operación
     * @param requesterRolId Rol activo del usuario
     * @param resource Recurso objetivo (ej. COMPANY, PATIENT)
     * @param action Acción a realizar (ej. CREATE, READ, UPDATE)
     * @param authContext Contexto adicional (ownership, guardianship, etc.)
     * 
     * @throws BusinessRuleViolationException si autorización denegada
     */
    void authorize(
        UserIdentityId requesterId,
        RolId requesterRolId,
        ResourceCatalog.BasicResource resource,
        ActionCatalog.BasicAction action,
        AuthorizationContext authContext
    );
    
    /**
     * Valida autorización SIN lanzar excepción (solo retorna boolean).
     * 
     * Útil cuando necesitas checkear permiso sin interrumpir flujo.
     * Ejemplo: mostrar/ocultar botones en UI, filtrar resultados.
     * 
     * @return true si autorizado, false si denegado
     */
    boolean checkAuthorization(
        UserIdentityId requesterId,
        RolId requesterRolId,
        ResourceCatalog.BasicResource resource,
        ActionCatalog.BasicAction action,
        AuthorizationContext authContext
    );
}