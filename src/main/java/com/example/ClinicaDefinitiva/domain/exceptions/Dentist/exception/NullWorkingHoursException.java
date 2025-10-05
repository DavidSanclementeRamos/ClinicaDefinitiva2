package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class NullWorkingHoursException extends DentistBusinessRuleViolationException {
    public NullWorkingHoursException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NULL_WORKING_HOURS, contexto, detalle);
    }
}
