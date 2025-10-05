package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class PendingAppointmentsWithin24HoursException extends AppointmentBusinessRuleViolationException {
    public PendingAppointmentsWithin24HoursException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.PENDING_APPOINTMENT, contexto, detalle);
    }
}
