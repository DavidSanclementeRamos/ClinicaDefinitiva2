package com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject;


import com.example.ClinicaDefinitiva.domain.errors.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Value Object para representar un NIT.
 */
public final class Nit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Regex: números con opcional guion y dígito de verificación
    private static final Pattern NIT_PATTERN = Pattern.compile("^\\d{5,12}(-\\d)?$");

    private final String value;

    public Nit(String value) {
        this.value = value;
    }

    /**
     * Fábrica segura para crear un NIT válido.
     *
     * @param rawNIT   cadena con el NIT
     * @return instancia válida de Nit
     * @throws ValueObjectValidationException si el NIT no cumple formato
     */
    public static Nit of(String rawNIT) {
        if (rawNIT == null || rawNIT.trim().isEmpty()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_COMPANY_MISSING_TAX_ID, EntityContext.COMPANY);
        }

        String normalized = rawNIT.trim();

        if (!NIT_PATTERN.matcher(normalized).matches()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_COMPANY_MISSING_TAX_ID, com.example.ClinicaDefinitiva.domain.errors.EntityContext.COMPANY);
        }

        return new Nit(normalized);
    }

    public String value() {
        return value;
    }

}
