package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.nulo;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.AppointmentBusinessRuleViolationException;

public class AppointmentEndDateMissingException extends AppointmentBusinessRuleViolationException {
    public AppointmentEndDateMissingException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NULL_END_DATE_APPOINTMENT, contexto, detalle);
    }
}
