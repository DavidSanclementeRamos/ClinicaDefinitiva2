package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.util.Objects;

public class ExpenseId {
    private final String value; // conserva String para flexibilidad

    public ExpenseId(String value) {
        this.value = Objects.requireNonNull(value, "ExpenseId value cannot be null");
    }

    public static ExpenseId fromLong(Long id) {
        if (id == null) return null;
        return new ExpenseId(String.valueOf(id));
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static ExpenseId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("ContractId string is empty");
        return new ExpenseId(trimmed);
    }

    public Long asLong() {
        if (this.value == null) return null;
        return Long.valueOf(this.value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) { /* usual */
        return o instanceof ExpenseId && value.equals(((ExpenseId) o).getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
