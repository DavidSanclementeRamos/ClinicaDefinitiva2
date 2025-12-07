package com.example.ClinicaDefinitiva.domain.administration.contable.valueObject;

import java.util.Objects;

/**
 * Value Object que representa el ID de un convenio.
 * Inmutable y con validaciones de negocio.
 */
public class ContractId {
    private final String value; // conserva String para flexibilidad

    public ContractId(String value) {

        this.value = Objects.requireNonNull(value, "ContractId value cannot be null");
    }

    public static ContractId fromLong(Long id) {
        if (id == null) return null;
        return new ContractId(String.valueOf(id));
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static ContractId fromString(String value) {
        if (value == null) throw new IllegalArgumentException("ContractId string is null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("ContractId string is empty");
        return new ContractId(trimmed);
    }

    public Long asLong() {
        return Long.valueOf(this.value);
    }

    public String getValue() {
        return value;
    }

}

