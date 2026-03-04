package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

/**
 * VO UserRolAssignmentId - Identificador único del agregado UserRolAssignment
 */
public  record UserRolAssignmentId(Long getValue) {

    public static UserRolAssignmentId of(Long value) {
        if (value == null ) {

            throw new ValueObjectValidationException(AuthorizationVoError.ERR_USER_ROL_ASSIGNMENT_ID_NULL, VOContext.AUTHORIZATION);
        }
        return new UserRolAssignmentId(value);
    }
}

