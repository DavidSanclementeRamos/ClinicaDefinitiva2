package com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.UUID;

public class UserId {
    private final String value;

    public UserId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static UserId fromString(String value) {
        if (value == null) {
            throw new ValueObjectValidationException(ErrorCatalogXD.ERR_ID_NULL, VOContext.DENTIST_ID);
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ValueObjectValidationException(ErrorCatalogXD.ERR_ID_BLANK,VOContext.DENTIST_ID );
        }
        return new UserId(trimmed);
    }

    public String getValue() {
        return value;
    }
}
