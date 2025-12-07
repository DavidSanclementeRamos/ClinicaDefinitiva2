package com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject;

import java.util.Objects;
import java.util.UUID;


/**
 * Value Object que representa el ID de una empresa.
 * Inmutable y con validaciones de negocio.
 */
public class CompanyId {
    private final String value;

    public CompanyId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static CompanyId generate (){
        return new CompanyId(UUID.randomUUID().toString());
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static CompanyId fromString(String value) {
        if (value == null) throw new IllegalArgumentException("InvoiceId string is null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceId string is empty");
        return new CompanyId(trimmed);
    }



    public String getValue() {
        return value;
    }
}
