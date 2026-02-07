package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

/**
 * VO RolId - Identificador único del agregado Rol
 */
public final class RolId {

    private final Long value;

    private RolId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("RolId must be a positive number");
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

