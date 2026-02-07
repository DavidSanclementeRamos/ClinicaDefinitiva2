package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.util.HashSet;
import java.util.Set;

/**
 * Servicio de dominio para reglas de negocio relacionadas con Roles.
 * Encapsula validaciones que requieren colaboración con repositorios.
 */
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol createCustom(RolEnum baseType, String description, Set<Permission> permissions) {
        validateUniqueDescription(description);
        Rol rol = new Rol(
                baseType,
                description,
                false,
                true,   // clones son editables
                true,   // clones son eliminables
                RolStatus.ACTIVE
        );
        rol.setPermissions(new HashSet<>(rol.getPermissions()));
        return rolRepository.save(rol);
    }

    public Rol cloneRole(Rol sourceRole, String newDescription) {
        validateUniqueDescription(newDescription);

        Rol clonedRole = new Rol(
                sourceRole.getRolEnum(),
                    newDescription,
                false,
                true,   // clones son editables
                true,   // clones son eliminables
                RolStatus.ACTIVE
        );
        clonedRole.setPermissions(new HashSet<>(sourceRole.getPermissions()));
        return rolRepository.save(clonedRole);
    }

    private void validateUniqueDescription(String description) {
        if (rolRepository.existsByDescription(description)) {
            throw new BusinessRuleViolationException(
                    RolError.ERR_ROL_DUPLICATE_DESCRIPTION,
                    EntityContext.ROL
            );
        }
    }
}
