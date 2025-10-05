package com.example.ClinicaDefinitiva.domain.exceptions.receptionist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ModelException;

public class ReceptionistBusinessRuleViolationException extends ModelException {
    public ReceptionistBusinessRuleViolationException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
