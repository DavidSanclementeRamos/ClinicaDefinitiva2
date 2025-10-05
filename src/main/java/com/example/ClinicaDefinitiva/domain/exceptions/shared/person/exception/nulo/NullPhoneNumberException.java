package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.nulo;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.NullValueException;

public class NullPhoneNumberException extends NullValueException {
    public NullPhoneNumberException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.BLANK_PHONE_NUMBER, contexto, detalle);
    }
}
