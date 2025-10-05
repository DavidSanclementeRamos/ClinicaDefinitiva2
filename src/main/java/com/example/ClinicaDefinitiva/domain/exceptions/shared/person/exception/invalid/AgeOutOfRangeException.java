package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.invalid;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.InvalidFormatException;


/**
 * Thrown when the provided age violates the allowed range for a given role or context.
 * Exhibits the ethical rule: the age must be within the clinically or professionally accepted boundaries.
 */
public class AgeOutOfRangeException extends InvalidFormatException {
    public AgeOutOfRangeException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_AGE, contexto, detalle);
    }
}
