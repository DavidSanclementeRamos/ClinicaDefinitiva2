package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.blank;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.BlankValueException;

public class BlankAddressException extends BlankValueException {
    public BlankAddressException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.BLANK_ADDRESS, contexto, detalle);
    }
}
