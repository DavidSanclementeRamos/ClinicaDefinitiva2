package com.example.ClinicaDefinitiva.exceptions;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;

public class DniDuplicadoException extends ClinicaDefinitivaException {
    public DniDuplicadoException( ContextoEntidad contexto, String detalle) {
        super(CatalogoError.DNI_DUPLICADO, contexto, detalle);
    }
}
