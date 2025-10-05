package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class TelefonoDuplicadoException extends ClinicaDefinitivaException {

    public TelefonoDuplicadoException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.TELEFONO_DUPLICADO, contexto, detalle);
    }
}
