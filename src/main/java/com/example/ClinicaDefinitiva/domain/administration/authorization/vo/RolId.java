package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.AuthorizationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

/**
 * VO RolId - Identificador único del agregado Rol
 */
public record  RolId(Long getValue) {


    public static RolId of(Long value) {
        if (value == null ) {
            throw new ValueObjectValidationException(AuthorizationVoError.ERR_ROL_ID_NULL, VOContext.AUTHORIZATION);
        }
        return new RolId(value);
    }

   }

