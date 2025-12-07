package com.example.ClinicaDefinitiva.domain.administration.contable.valueObject;

import java.util.Objects;

/**
 * Value Object que representa el ID de ThirdParties.
 * Inmutable y con validaciones de negocio.
 */
public class OpeningBalanceId {
    private final String value; // conserva String para flexibilidad

    public OpeningBalanceId(String value) {
        this.value = Objects.requireNonNull(value, "OpeningBalanceId value cannot be null");
    }

    public static OpeningBalanceId fromLong(Long id) {
        if (id == null) return null;
        return new OpeningBalanceId(String.valueOf(id));
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static OpeningBalanceId fromString(String value) {
        if (value == null) throw new IllegalArgumentException("OpeningBalanceId string is null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("OpeningBalanceId string is empty");
        return new OpeningBalanceId(trimmed);
    }

    public Long asLong() {
        return Long.valueOf(this.value);
    }

    public String getValue() {
        return value;
    }

}
