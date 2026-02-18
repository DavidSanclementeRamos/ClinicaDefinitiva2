package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

/**
 * VO UserRolAssignmentId - Identificador único del agregado UserRolAssignment
 */
public final class UserRolAssignmentId {

    private final Long value;

    private UserRolAssignmentId(Long value) {
        if (value == null || value <= 0) {

            throw new ValueObjectValidationException(AuthorizationVoError.ERR_USER_ROL_ASSIGNMENT_ID_NULL, VOContext.AUTHORIZATION);
        }
        this.value = value;
    }

    public static UserRolAssignmentId of(Long value) {
        return new UserRolAssignmentId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRolAssignmentId)) return false;
        UserRolAssignmentId that = (UserRolAssignmentId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserRolAssignmentId{" + value + '}';
    }
}

