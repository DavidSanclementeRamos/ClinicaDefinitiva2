package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.blank;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.BlankValueException;

public class BlankAgeException extends BlankValueException {
    public BlankAgeException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
