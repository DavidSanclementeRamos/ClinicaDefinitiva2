package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.VoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class Name {

    private static final int MAX_NAME_LENGTH = 255;
    private final String value;

    private Name(String value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoError.ERR_NAME_NULL,
                    VOContext.ACCOUNTING
            );
        }
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new ValueObjectValidationException(
                    VoError.ERR_NAME_BLANK,
                    VOContext.ACCOUNTING
            );
        }
        if (normalized.length() > MAX_NAME_LENGTH) {
            throw new ValueObjectValidationException(
                    VoError.ERR_NAME_TOO_LONG,
                    VOContext.ACCOUNTING
            );
        }
        this.value = normalized;
    }

    public static Name of(String value) {
        return new Name(value);
    }

    public String getValue() { return value; }

    @Override
    public String toString() { return value; }
}
