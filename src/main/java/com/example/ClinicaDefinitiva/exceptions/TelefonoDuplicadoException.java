package com.example.ClinicaDefinitiva.exceptions;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;

public class TelefonoDuplicadoException extends ClinicaDefinitivaException {

    public TelefonoDuplicadoException( ContextoEntidad contexto, String detalle) {
        super(CatalogoError.TELEFONO_DUPLICADO, contexto, detalle);
    }
}
