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
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization.RolError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Servicio de autorización - punto de entrada único para validaciones
 */
public class AuthorizationService {
    private final RolRepository rolRepository;
    private final List<PermissionPolicy> policies;

    public AuthorizationService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
        this.policies = new ArrayList<>();

        // Orden de evaluación (por prioridad descendente):
        // 1. Ownership (300) - más restrictivo
        // 2. Sector/Specialty (200) - restricciones específicas
        // 3. RBAC Base (100) - permisos generales

        policies.add(new RoleBasedPolicy());
        policies.add(new OwnershipPolicy());
        policies.add(new SectorBasedPolicy());
        policies.add(new SpecialtyBasedPolicy());

        // Ordenar por prioridad
        policies.sort(Comparator.comparingInt(PermissionPolicy::getPriority).reversed());
    }

    /**
     * Verifica autorización con todas las políticas aplicables
     * TODAS las políticas que apliquen deben permitir la operación
     */
   /** public boolean isAuthorized(Rol rol, SecurityContext context) {
        for (PermissionPolicy policy : policies) {
            // Solo evaluar políticas que apliquen al contexto
            if (!policy.appliesTo(context)) {
                continue;
            }

            // Si alguna política aplicable deniega, denegar
            if (!policy.isAllowed(rol, context)) {
                return false;
            }
        }
        return true;

        }*/
        // AuthorizationService
        public boolean isAuthorized(RolId rolId, SecurityContext context) {
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new DomainAggregateException(RolError.ERR_ROL_NOT_FOUND, EntityContext.ROL));
            for (PermissionPolicy policy : policies) {
                if (!policy.appliesTo(context)) continue;
                if (!policy.isAllowed(rol, context)) return false;
            }
            return true;
        }



    /**
     * Método de conveniencia para permisos simples sin contexto ABAC
     */
    public boolean hasPermission(RolId rol, UserIdentityId userIdentityId, ResourceCatalog resource, ActionCatalog action) {
        Permission permission = Permission.of(resource, action);
        SecurityContext context = SecurityContext.builder(permission, userIdentityId).build();
        return isAuthorized(rol, context);
    }
}
