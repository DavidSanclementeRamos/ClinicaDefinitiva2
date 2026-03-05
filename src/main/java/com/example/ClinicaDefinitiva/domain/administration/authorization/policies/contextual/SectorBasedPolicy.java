package com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.PermissionPolicy;

/**
 * Política basada en sector - para RECEPTIONIST con sectores específicos
 * Ejemplo: solo RRHH puede eliminar dentistas
 */
public class SectorBasedPolicy implements PermissionPolicy {

    @Override
    public boolean appliesTo(SecurityContext context) {
        return ActionCatalog.of(ActionCatalog.BasicAction.DELETE).equals(context.getPermission().getAction()) &&
                ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST).equals(context.getPermission().getResource());
    }

    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        // Solo aplica para RECEPTIONIST intentando eliminar DENTIST
        if (rol.getRolEnum() != RolEnum.RECEPTIONIST) {
            return true; // No restringe otros roles
        }

        if (!appliesTo(context)) {
            return true; // No aplica a esta operación
        }

        String sector = context.getAttribute("sector", String.class).orElse("");

        // Solo puede eliminar dentistas si es del sector de RRHH
        return "RECURSOS_HUMANOS".equalsIgnoreCase(sector);
    }

    @Override
    public int getPriority() {
        return 200; // Prioridad media
    }
}