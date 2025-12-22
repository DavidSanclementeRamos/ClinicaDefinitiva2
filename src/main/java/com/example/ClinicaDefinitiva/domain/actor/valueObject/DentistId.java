package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.UUID;

public final class  DentistId {
    private final String value;

    public DentistId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static DentistId generate (){
        return new DentistId(UUID.randomUUID().toString());
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static DentistId fromString(String value) {
        if (value == null) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_NULL, ContextoEntidad.valueOf(""));
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_BLANK, ContextoEntidad.valueOf(""));
        }
        return new DentistId(trimmed);
    }

    public String getValue() {
        return value;
    }
}
