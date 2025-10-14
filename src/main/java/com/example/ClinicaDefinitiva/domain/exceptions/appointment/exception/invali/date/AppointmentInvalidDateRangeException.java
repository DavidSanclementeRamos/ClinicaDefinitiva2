package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.invali.date;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.AppointmentBusinessRuleViolationException;

public class AppointmentInvalidDateRangeException extends AppointmentBusinessRuleViolationException {
    public AppointmentInvalidDateRangeException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_DATE_RANGE_APPOINTMENT, contexto, detalle);
    }
}
