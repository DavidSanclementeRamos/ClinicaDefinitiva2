package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class FutureAppointmentsExistException extends AppointmentBusinessRuleViolationException {
    public FutureAppointmentsExistException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.FUTURE_APPOINTMENT, contexto, detalle);
    }
}
