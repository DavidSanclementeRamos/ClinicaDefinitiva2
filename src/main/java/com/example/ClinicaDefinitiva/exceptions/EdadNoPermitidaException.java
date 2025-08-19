package com.example.ClinicaDefinitiva.exceptions;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;

public class EdadNoPermitidaException extends ClinicaDefinitivaException {
    public EdadNoPermitidaException( ContextoEntidad contexto, String detalle) {
        super(CatalogoError.EDAD_NO_PERMITIDA, contexto, detalle);
    }
}
