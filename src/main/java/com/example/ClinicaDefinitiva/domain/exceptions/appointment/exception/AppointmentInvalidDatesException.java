package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class AppointmentInvalidDatesException extends AppointmentBusinessRuleViolationException{
    public AppointmentInvalidDatesException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
