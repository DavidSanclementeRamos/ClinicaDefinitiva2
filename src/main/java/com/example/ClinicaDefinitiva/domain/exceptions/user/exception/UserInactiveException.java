package com.example.ClinicaDefinitiva.domain.exceptions.user.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class UserInactiveException extends UserBusinessRuleViolationException {
    public UserInactiveException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INACTIVE_USER, contexto, detalle);
    }
}
