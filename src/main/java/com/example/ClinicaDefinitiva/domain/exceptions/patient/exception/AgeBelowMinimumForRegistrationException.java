package com.example.ClinicaDefinitiva.domain.exceptions.patient.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class AgeBelowMinimumForRegistrationException extends PatientBusinessRuleViolationException{
    public AgeBelowMinimumForRegistrationException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_AGE, contexto, detalle);
    }
}
