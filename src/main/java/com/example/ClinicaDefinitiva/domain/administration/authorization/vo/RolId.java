package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

/**
 * VO RolId - Identificador único del agregado Rol
 */
public final class RolId {

    private final Long value;

    private RolId(Long value) {
        if (value == null || value <= 0) {
            throw new ValueObjectValidationException(AuthorizationVoError.ERR_ROL_ID_NULL, VOContext.AUTHORIZATION);
        }
        this.value = value;
    }

    public static RolId of(Long value) {
        return new RolId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolId)) return false;
        RolId rolId = (RolId) o;
        return value.equals(rolId.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }

    @Override
    public String toString() {
        return "RolId{" + value + '}';
    }
}

