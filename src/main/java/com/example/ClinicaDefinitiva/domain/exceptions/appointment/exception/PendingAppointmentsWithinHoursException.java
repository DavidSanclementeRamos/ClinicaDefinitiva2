package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class PendingAppointmentsWithinHoursException extends PendingAppointmentsException {
    public PendingAppointmentsWithinHoursException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.PENDING_APPOINTMENT, contexto, detalle);
    }
}
