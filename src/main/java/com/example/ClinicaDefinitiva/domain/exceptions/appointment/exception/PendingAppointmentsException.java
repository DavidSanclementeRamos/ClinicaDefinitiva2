package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class PendingAppointmentsException extends AppointmentBusinessRuleViolationException{
    public PendingAppointmentsException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.PENDING_APPOINTMENT, contexto, detalle);
    }
}
