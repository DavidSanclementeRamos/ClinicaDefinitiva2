package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

/**
 * Se lanza cuando el valor textual de una especialidad no pertenece al catálogo permitido.
 * Exhibe la frontera semántica del dominio clínico.
 */
public class InvalidSpecialtyValueException extends DentistBusinessRuleViolationException {
    public InvalidSpecialtyValueException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_SPECIALTY_VALUE, contexto, detalle);
    }
}
