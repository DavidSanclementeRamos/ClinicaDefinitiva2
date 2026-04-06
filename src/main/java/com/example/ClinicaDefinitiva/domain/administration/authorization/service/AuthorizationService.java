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
 *
 * Nivel auxiliar — requiresSectorContext:
 *   Usado por DefaultAuthorizationHelper para decidir si debe cargar
 *   el Receptionist antes de construir el SecurityContext.
 *   Delega en SectorBasedPolicy.appliesTo() sin exponer esa clase al helper.
 */
@Service
public class AuthorizationService {

    private final RolRepository rolRepository;
    private final RoleBasedPolicy roleBasedPolicy;
    private final SectorBasedPolicy sectorBasedPolicy;
    private final List<PermissionPolicy> abacPolicies;

    public AuthorizationService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
        this.roleBasedPolicy = new RoleBasedPolicy();

        // SectorBasedPolicy se guarda como campo para poder consultarla
        // en requiresSectorContext() sin acoplar el helper a la política directamente
        this.sectorBasedPolicy = new SectorBasedPolicy();

        // Lista ABAC: usa la misma instancia para no duplicar el objeto
        this.abacPolicies = new ArrayList<>();
        this.abacPolicies.add(new OwnershipPolicy());
        this.abacPolicies.add(sectorBasedPolicy);
        this.abacPolicies.add(new SpecialtyBasedPolicy());
        this.abacPolicies.sort(Comparator.comparingInt(PermissionPolicy::getPriority).reversed());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NIVEL 1: RBAC — usado exclusivamente por AuthorizationAspect (@RequiresPermission)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Verifica si el rol tiene el permiso base según RoleBasedPolicy.
     * No requiere instancia de Rol desde BD: opera sobre el enum del rol cargado en sesión.
     */
    public boolean isAllowedByRole(Rol rol, SecurityContext context) {
        return roleBasedPolicy.isAllowed(rol, context);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NIVEL AUXILIAR — consultado por DefaultAuthorizationHelper antes de
    // construir el SecurityContext completo
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Indica si la operación requiere que el helper cargue el sector del actor
     * antes de construir el SecurityContext.
     *
     * Delega en SectorBasedPolicy.appliesTo() usando un contexto mínimo
     * (solo el permiso — el userId no es necesario para esta consulta).
     *
     * Esto permite que DefaultAuthorizationHelper tome la decisión de cargar
     * el Receptionist sin conocer directamente a SectorBasedPolicy, preservando
     * el principio de que el helper solo habla con AuthorizationService.
     *
     * @param resource recurso de la operación
     * @param action   acción de la operación
     * @return true si SectorBasedPolicy aplica a esta operación y por tanto
     *         el sector del actor es necesario en el SecurityContext
     */
    public boolean requiresSectorContext(
            ResourceCatalog.BasicResource resource,
            ActionCatalog.BasicAction action) {

        Permission permission = Permission.of(
                ResourceCatalog.of(resource),
                ActionCatalog.of(action)
        );
        // userId null es seguro aquí: SectorBasedPolicy.appliesTo() solo lee
        // el permiso del contexto, nunca el requestingUserId
        SecurityContext minimalContext = SecurityContext.builder(permission, null).build();
        return sectorBasedPolicy.appliesTo(minimalContext);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NIVEL 2: ABAC — usado exclusivamente por DefaultAuthorizationHelper
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Verifica autorización contextual (ABAC).
     * NO evalúa RoleBasedPolicy — ese chequeo ya fue hecho por @RequiresPermission.
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
     */
    public boolean isAuthorized(RolId rolId, SecurityContext context) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new DomainAggregateException(RolError.ERR_ROL_NOT_FOUND, EntityContext.ROL));

        if (!roleBasedPolicy.isAllowed(rol, context)) return false;

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