package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;



import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.regex.Pattern;

public final class Nit {

    private static final Pattern NIT_PATTERN = Pattern.compile("^\\d{5,12}(-\\d)?$");
    private final String value;

    private Nit(String value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_NIT_NULL,
                    VOContext.ACCOUNTING
            );
        }
        String normalized = value.trim();
        if (!NIT_PATTERN.matcher(normalized).matches()) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_NIT_INVALID_FORMAT,
                    VOContext.ACCOUNTING
            );
        }
        this.value = normalized;
    }

    public static Nit of(String value) {
        return new Nit(value);
    }

    public String getValue() { return value; }
}
