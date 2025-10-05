package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ModelException;

public class DentistBusinessRuleViolationException extends ModelException {
    public DentistBusinessRuleViolationException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
