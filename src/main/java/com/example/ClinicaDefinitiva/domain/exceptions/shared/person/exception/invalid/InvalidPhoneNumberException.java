package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.invalid;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.InvalidFormatException;

public class InvalidPhoneNumberException extends InvalidFormatException {
    public InvalidPhoneNumberException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_PHONE_NUMBER, contexto, detalle);
    }
}
