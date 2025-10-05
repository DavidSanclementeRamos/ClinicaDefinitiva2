package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

/**
 * Thrown when a Value Object receives a birthdate that violates chronological legitimacy.
 * Ethically invalidates any claim of existence beyond the current temporal boundary.
 */


public class DateOfBirthInFutureException extends TemporalInvalidationException{
    public DateOfBirthInFutureException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.FUTURE_DATE_OF_BIRTH, contexto, detalle);
    }
}
