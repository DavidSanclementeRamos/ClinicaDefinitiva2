package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

/**
 * Se lanza cuando el conjunto de especialidades está vacío o no ha sido proporcionado.
 * Exhibe la regla de legitimidad: toda entidad debe declarar al menos una especialidad para ser considerada trazable.
 */
public class EmptySpecialtySetException extends DentistBusinessRuleViolationException {
    public EmptySpecialtySetException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.EMPTY_SPECIALTY_SET, contexto, detalle);
    }
}
