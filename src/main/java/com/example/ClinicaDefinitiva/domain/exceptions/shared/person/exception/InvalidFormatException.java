package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class InvalidFormatException extends MissingSemanticValueException {
    public InvalidFormatException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
