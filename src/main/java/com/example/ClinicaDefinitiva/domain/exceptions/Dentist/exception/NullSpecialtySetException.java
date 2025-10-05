package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class NullSpecialtySetException extends DentistBusinessRuleViolationException {
    public NullSpecialtySetException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NULL_SPECIALTY, contexto, detalle);
    }
}
