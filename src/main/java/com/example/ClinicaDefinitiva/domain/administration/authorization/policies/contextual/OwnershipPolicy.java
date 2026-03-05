package com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.PermissionPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

/**
 * Política de propiedad - usuarios solo pueden operar sobre sus propios recursos
 */
public class OwnershipPolicy implements PermissionPolicy {

    @Override
    public boolean appliesTo(SecurityContext context) {
        ResourceCatalog resource = (context.getPermission().getResource());
        // Solo aplica a recursos con concepto de "ownership"
        return ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT).equals(resource) ||
                ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN).equals(resource);
    }

    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        ResourceCatalog resource = context.getPermission().getResource();

        // PATIENT: solo puede ver/modificar sus propios datos
        if (ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT).equals(resource) && rol.getRolEnum() == RolEnum.PATIENT) {
            return checkOwnership(context);
        }

        // GUARDIAN: solo puede ver/modificar pacientes bajo su tutela
        if (ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT).equals(resource) && rol.getRolEnum() == RolEnum.GUARDIAN) {
            return checkGuardianship(context);
        }

        // GUARDIAN: solo puede ver/modificar sus propios datos
        if (ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN).equals(resource) && rol.getRolEnum() == RolEnum.GUARDIAN) {
            return checkOwnership(context);
        }

        // No restringe otros casos
        return true;
    }

    private boolean checkOwnership(SecurityContext context) {
        UserIdentityId requestingUserIdentityId = context.getRequestingUserId();
        UserIdentityId resourceOwnerId = context.getAttribute("resourceOwnerId", UserIdentityId.class).orElse(null);

        if (requestingUserIdentityId == null || resourceOwnerId == null) {
            return false; // Sin información suficiente, denegar
        }

        return requestingUserIdentityId.equals(resourceOwnerId);
    }

    private boolean checkGuardianship(SecurityContext context) {
        Long requestingGuardianId = context.getRequestingUserId().value();
        Long patientGuardianId = context.getAttribute("patientGuardianId", Long.class).orElse(null);

        if (patientGuardianId == null) {
            return false; // Paciente sin tutor o sin información
        }

        return requestingGuardianId.equals(patientGuardianId);
    }

    @Override
    public int getPriority() {
        return 300;
    }
}
