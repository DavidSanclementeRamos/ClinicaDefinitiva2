package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public class SharedPersonBusinessRuleViolationException extends ValueObjectValidationException {
    public SharedPersonBusinessRuleViolationException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
