package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;


public final class ReceptionId {
private final String value;

    public ReceptionId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static ReceptionId fromString(String value) {
        if (value == null) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_NULL, VOContext.RECEPTION_ID);

        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_BLANK, VOContext.RECEPTION_ID);
        }
        return new ReceptionId(trimmed);
    }
    public String getValue() {
        return value;
    }
}
