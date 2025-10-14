package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.invali.date;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.AppointmentBusinessRuleViolationException;

public class AppointmentOutsideAvailabilityException extends AppointmentBusinessRuleViolationException {
    public AppointmentOutsideAvailabilityException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.OUTSIDE_AVAILABILITY_APPOINTMENT, contexto, detalle);
    }
}
