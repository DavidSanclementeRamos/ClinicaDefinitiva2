package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class UserStatusException extends ClinicaDefinitivaException {

    public UserStatusException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.USER_STATUS, contexto, detalle);
    }
}
