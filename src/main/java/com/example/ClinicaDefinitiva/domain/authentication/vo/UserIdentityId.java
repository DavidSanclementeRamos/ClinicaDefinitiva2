package com.example.ClinicaDefinitiva.domain.authentication.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public record UserIdentityId(Long value) {


    public static UserIdentityId from(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(AuthenticationVoError.ERR_USER_ID_INVALID, VOContext.AUTHENTICATION);
        }

        return new UserIdentityId(value);
    }
}
