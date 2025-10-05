package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.blank;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.BlankValueException;

public class BlankPhoneNumberException extends BlankValueException {
    public BlankPhoneNumberException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.BLANK_PHONE_NUMBER, contexto, detalle);
    }
}
