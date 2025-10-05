package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.TemporalInvalidationException;

/**
 * Se lanza cuando el rango horario definido no respeta la secuencia temporal legítima.
 * Exhibe la regla ética: todo horario de trabajo debe tener una hora de inicio anterior a la de fin.
 */


public class StartTimeAfterEndTimeException extends TemporalInvalidationException {
    public StartTimeAfterEndTimeException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.START_TIME_AFTER_END_TIME_WORKING_HOURS, contexto, detalle);
    }
}
