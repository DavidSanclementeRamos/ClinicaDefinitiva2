package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;


public final class PatientId {
    private final String value;

    public PatientId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static PatientId fromString(String value) {
        if (value == null) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_NULL, ContextoEntidad.PATIENT);

        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ID_BLANK, ContextoEntidad.PATIENT);

        }
        return new PatientId(trimmed);
    }


    public String getValue() {
        return value;
    }
}
