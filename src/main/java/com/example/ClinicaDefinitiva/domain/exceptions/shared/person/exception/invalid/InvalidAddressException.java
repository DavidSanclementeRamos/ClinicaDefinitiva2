package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.invalid;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.InvalidFormatException;

public class InvalidAddressException extends InvalidFormatException {
    public InvalidAddressException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
