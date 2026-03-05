package com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.PermissionPolicy;

import java.util.Set;

/**
 * Política basada en especialidad - dentistas solo ven servicios de su especialidad
 */
public class SpecialtyBasedPolicy implements PermissionPolicy {

    @Override
    public boolean appliesTo(SecurityContext context) {
        return ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE).equals(context.getPermission().getResource());
    }

    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        // Solo aplica para DENTIST consultando servicios
        if (rol.getRolEnum() != RolEnum.DENTIST) {
            return true; // No restringe otros roles
        }

        if (!appliesTo(context)) {
            return true;
        }

        @SuppressWarnings("unchecked")
        Set<String> dentistSpecialties = context.getAttribute("dentistSpecialties", Set.class).orElse(null);
        String serviceSpecialty = context.getAttribute("serviceSpecialty", String.class).orElse(null);

        // Si no hay información de especialidades, permitir (será validado en capa de negocio)
        if (dentistSpecialties == null || serviceSpecialty == null) {
            return true;
        }

        // Verificar que el dentista tenga la especialidad del servicio
        return dentistSpecialties.contains(serviceSpecialty);
    }

    @Override
    public int getPriority() {
        return 200; // Prioridad media
    }
}
