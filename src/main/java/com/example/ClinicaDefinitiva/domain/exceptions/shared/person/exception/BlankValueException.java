package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class BlankValueException extends MissingSemanticValueException{
    public BlankValueException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
