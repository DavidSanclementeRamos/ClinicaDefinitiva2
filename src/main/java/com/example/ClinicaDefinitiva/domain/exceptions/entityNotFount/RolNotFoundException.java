package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class RolNotFoundException extends ClinicaDefinitivaException {

   public RolNotFoundException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.ROLE_NOT_FOUND, contexto, detalle);
    }
}
