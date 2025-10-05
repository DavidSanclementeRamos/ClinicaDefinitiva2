package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class EdadNoPermitidaException extends ClinicaDefinitivaException {
    public EdadNoPermitidaException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.EDAD_NO_PERMITIDA, contexto, detalle);
    }
}
