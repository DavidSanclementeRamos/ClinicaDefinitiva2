package com.example.ClinicaDefinitiva.domain.exceptions.patient.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.ModelException;

public class PatientBusinessRuleViolationException extends ModelException {
    public PatientBusinessRuleViolationException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
