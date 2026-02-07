package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.util.Objects;
/**
 * Value Object que representa el ID de un registro accounting.
 * Inmutable y con validaciones de negocio.
 */
public class JournalEntryId {
    // si son registros contables journal entries, si son operaciones transactions, movimiento accounting

    private final String value; // conserva String para flexibilidad

    public JournalEntryId(String value) {
        this.value = Objects.requireNonNull(value, "JournalEntryId value cannot be null");
    }

    public static JournalEntryId fromLong(Long id) {
        if (id == null) return null;
        return new JournalEntryId(String.valueOf(id));
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static JournalEntryId fromString(String value) {
        if (value == null) throw new IllegalArgumentException("JournalEntryId string is null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("JournalEntryId string is empty");
        return new JournalEntryId(trimmed);
    }

    public Long asLong() {
        return Long.valueOf(this.value);
    }

    public String getValue() {
        return value;
    }

}
