package com.example.ClinicaDefinitiva.domain.administration.valueObject;

import java.util.Objects;

public class ContractId {
    private final String value; // conserva String para flexibilidad

    public ContractId(String value) {
        this.value = Objects.requireNonNull(value, "ContractId value cannot be null");
    }

    public static ContractId fromLong(Long id) {
        if (id == null) return null;
        return new ContractId(String.valueOf(id));
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static ContractId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("ContractId string is empty");
        return new ContractId(trimmed);
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
        return o instanceof ContractId && value.equals(((ContractId) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}

