package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.PermissionPolicy;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.RoleBasedPolicy;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual.OwnershipPolicy;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual.SectorBasedPolicy;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual.SpecialtyBasedPolicy;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.RolError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Servicio de autorización con dos niveles bien diferenciados:
 *
 * Nivel 1 — RBAC (isAllowedByRole):
 *   Usado por @RequiresPermission (AOP).
 *   Solo evalúa RoleBasedPolicy.
 *   Pregunta: "¿El tipo de rol tiene este permiso base?"
 *   Sin BD, sin contexto, sin ABAC.
 *
 * Nivel 2 — ABAC (isAuthorizedByContext):
 *   Usado por DefaultAuthorizationHelper dentro del ApplicationService.
 *   Evalúa OwnershipPolicy, SectorBasedPolicy, SpecialtyBasedPolicy.
 *   Pregunta: "¿En este contexto específico está permitido?"
 *   Requiere contexto: sector, ownerId, guardianId, specialties.
 *
 * El método isAuthorized() combina ambos para compatibilidad con código existente
 * que aún no usa la separación.
 */
@Service
public class AuthorizationService {

    private final RolRepository rolRepository;
    private final RoleBasedPolicy roleBasedPolicy;
    private final List<PermissionPolicy> abacPolicies;

    public AuthorizationService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
        this.roleBasedPolicy = new RoleBasedPolicy();

        // Solo políticas ABAC (contextuales) — NO incluye RoleBasedPolicy
        this.abacPolicies = new ArrayList<>();
        this.abacPolicies.add(new OwnershipPolicy());
        this.abacPolicies.add(new SectorBasedPolicy());
        this.abacPolicies.add(new SpecialtyBasedPolicy());
        this.abacPolicies.sort(Comparator.comparingInt(PermissionPolicy::getPriority).reversed());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NIVEL 1: RBAC — usado exclusivamente por AuthorizationAspect (@RequiresPermission)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Verifica si el rol tiene el permiso base según RoleBasedPolicy.
     * No requiere instancia de Rol desde BD: opera sobre el enum del rol cargado en sesión.
     *
     * @param rol Agregado Rol ya cargado (viene de CustomUserDetails)
     * @param context SecurityContext con el permiso solicitado
     * @return true si el rol tiene el permiso base
     */
    public boolean isAllowedByRole(Rol rol, SecurityContext context) {
        return roleBasedPolicy.isAllowed(rol, context);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NIVEL 2: ABAC — usado exclusivamente por DefaultAuthorizationHelper
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Verifica autorización contextual (ABAC) para políticas de sector, ownership y especialidad.
     * NO evalúa RoleBasedPolicy — ese chequeo ya fue hecho por @RequiresPermission.
     *
     * @param rolId ID del rol del requester
     * @param context SecurityContext con atributos ABAC (sector, ownerId, guardianId, etc.)
     * @return true si todas las políticas ABAC aplicables permiten la operación
     */
    public boolean isAuthorizedByContext(RolId rolId, SecurityContext context) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new DomainAggregateException(RolError.ERR_ROL_NOT_FOUND, EntityContext.ROL));

        for (PermissionPolicy policy : abacPolicies) {
            if (!policy.appliesTo(context)) continue;
            if (!policy.isAllowed(rol, context)) return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // COMBINADO — mantiene compatibilidad con código que usa isAuthorized()
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Evalúa RBAC + ABAC en un solo paso.
     * Úsalo solo cuando NO tengas la anotación @RequiresPermission en el método.
     *
     * @param rolId ID del rol del requester
     * @param context SecurityContext completo
     * @return true si RBAC y todas las políticas ABAC aplicables permiten la operación
     */
    public boolean isAuthorized(RolId rolId, SecurityContext context) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new DomainAggregateException(RolError.ERR_ROL_NOT_FOUND, EntityContext.ROL));

        // RBAC base
        if (!roleBasedPolicy.isAllowed(rol, context)) return false;

        // ABAC contextual
        for (PermissionPolicy policy : abacPolicies) {
            if (!policy.appliesTo(context)) continue;
            if (!policy.isAllowed(rol, context)) return false;
        }
        return true;
    }

    /**
     * Método de conveniencia para permisos simples sin contexto ABAC.
     */
    public boolean hasPermission(RolId rolId, UserIdentityId userIdentityId,
                                  ResourceCatalog resource, ActionCatalog action) {
        Permission permission = Permission.of(resource, action);
        SecurityContext context = SecurityContext.builder(permission, userIdentityId).build();
        return isAuthorized(rolId, context);
    }
}
