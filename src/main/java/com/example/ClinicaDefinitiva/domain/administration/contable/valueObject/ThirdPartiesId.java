package com.example.ClinicaDefinitiva.domain.administration.contable.valueObject;

import java.util.Objects;

/**
 * Value Object que representa el ID de un tercero.
 * Inmutable y con validaciones de negocio.
 */
public class ThirdPartiesId {

    private final String value;

    public ThirdPartiesId(String value) {
        this.value = Objects.requireNonNull(value, "ThirdPartiesId value cannot be null");
    }

    public static ThirdPartiesId fromLong(Long id) {
        if (id == null) return null;
        return new ThirdPartiesId(String.valueOf(id));
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static ThirdPartiesId fromString(String value) {
        if (value == null)  throw new IllegalArgumentException("ThirdPartiesId string is null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("ThirdPartiesId string is empty");
        return new ThirdPartiesId(trimmed);
    }

    public Long asLong() {
        return Long.valueOf(this.value);
    }

    public String getValue() {
        return value;
    }

}
