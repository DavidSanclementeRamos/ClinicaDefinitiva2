package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.nulo;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.AppointmentBusinessRuleViolationException;

public class AppointmentStartDateMissingException extends AppointmentBusinessRuleViolationException {
    public AppointmentStartDateMissingException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NULL_START_DATE_APPOINTMENT, contexto, detalle);
    }
}
