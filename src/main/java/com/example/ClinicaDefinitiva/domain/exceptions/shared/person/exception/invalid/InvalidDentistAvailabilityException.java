package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.invalid;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.InvalidFormatException;

public class InvalidDentistAvailabilityException extends InvalidFormatException {
    public InvalidDentistAvailabilityException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
