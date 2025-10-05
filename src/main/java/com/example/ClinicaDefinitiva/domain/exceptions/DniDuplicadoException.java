package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class DniDuplicadoException extends ClinicaDefinitivaException {
    public DniDuplicadoException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.DNI_DUPLICADO, contexto, detalle);
    }
}
