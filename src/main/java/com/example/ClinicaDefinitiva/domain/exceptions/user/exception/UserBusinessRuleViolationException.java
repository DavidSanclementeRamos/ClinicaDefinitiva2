package com.example.ClinicaDefinitiva.domain.exceptions.user.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class UserBusinessRuleViolationException extends ClinicaDefinitivaException {
    public UserBusinessRuleViolationException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
