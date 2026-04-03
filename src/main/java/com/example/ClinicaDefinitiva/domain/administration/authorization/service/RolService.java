package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;

import org.springframework.stereotype.Service;

/**
 * Servicio de dominio para reglas de negocio relacionadas con Roles.
 * Encapsula validaciones que requieren colaboración con repositorios.
 */
@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol createCustom(RolEnum baseType, String description) {
        validateUniqueDescription(description);
        return Rol.createCustom(baseType, description);
    }

    public Rol cloneRole(Rol sourceRole, String newDescription) {
        validateUniqueDescription(newDescription);
        return Rol.cloneFrom(sourceRole, newDescription);
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