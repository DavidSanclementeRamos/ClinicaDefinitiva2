package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

/**
 * VO UserRolAssignmentId - Identificador único del agregado UserRolAssignment
 */
public final class UserRolAssignmentId {

    private final Long value;

    private UserRolAssignmentId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("UserRolAssignmentId must be a positive number");
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

