package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class ValueObjectValidationException extends ClinicaDefinitivaException {

    public ValueObjectValidationException(ErrorCatalog catalogo, ContextoEntidad contexto, ErrorCatalog code, ErrorCatalog key, ErrorCatalog mensaje) {
        super(catalogo, contexto, code, key, mensaje);
    }
}
