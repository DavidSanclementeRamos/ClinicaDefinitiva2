package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.invalid;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.InvalidFormatException;

public class InvalidDateOfBirthException extends InvalidFormatException {
    public InvalidDateOfBirthException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_DATE_OF_BIRTH, contexto, detalle);
    }
}
