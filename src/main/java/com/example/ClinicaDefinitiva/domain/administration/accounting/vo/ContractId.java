package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.util.Objects;

/**
 * Value Object que representa el ID de un convenio.
 * Inmutable y con validaciones de negocio.
 */
public class ContractId {
    private final Long value; // conserva String para flexibilidad

    public ContractId(Long value) {

        this.value = Objects.requireNonNull(value, "ContractId value cannot be null");
    }


    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static ContractId of(Long value) {
        if (value == null) throw new IllegalArgumentException("ContractId string is null");
        return new ContractId(value);
    }


    public Long asLong() {
        return Long.valueOf(this.value);
    }

    public Long getValue() {
        return value;
    }

}

