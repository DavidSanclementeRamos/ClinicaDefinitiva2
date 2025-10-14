package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class AppointmentTimeNotAvailableException extends AppointmentBusinessRuleViolationException{
    public AppointmentTimeNotAvailableException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.TIME_NO_AVAILABILITY_APPOINTMENT , contexto, detalle);
    }
}
