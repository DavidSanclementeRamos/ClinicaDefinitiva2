package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.util.Objects;
/**
 * Value Object que representa el ID de un plan accounting.
 * Inmutable y con validaciones de negocio.
 */
public class LedgerAccountId {
    // cuenta accounting
    private final String value; // conserva String para flexibilidad

    public LedgerAccountId(String value) {
        this.value = Objects.requireNonNull(value, "LedgerAccountId value cannot be null");
    }

    public static LedgerAccountId fromLong(Long id) {
        if (id == null) return null;
        return new LedgerAccountId(String.valueOf(id));
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static LedgerAccountId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("LedgerAccountId string is empty");
        return new LedgerAccountId(trimmed);
    }

    public Long asLong() {
        return Long.valueOf(this.value);
    }

    public String getValue() {
        return value;
    }

}
