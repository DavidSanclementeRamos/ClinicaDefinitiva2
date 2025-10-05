package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class TemporalInconsistencyException extends DentistBusinessRuleViolationException {
    public TemporalInconsistencyException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
