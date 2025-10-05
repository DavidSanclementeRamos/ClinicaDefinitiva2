package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.nulo;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.NullValueException;

public class NullAddressException extends NullValueException {
    public NullAddressException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NULL_ADDRESS , contexto, detalle);
    }
}
