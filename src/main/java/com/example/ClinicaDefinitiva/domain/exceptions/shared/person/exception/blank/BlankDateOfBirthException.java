package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.blank;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.BlankValueException;

public class BlankDateOfBirthException extends BlankValueException {
    public BlankDateOfBirthException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
