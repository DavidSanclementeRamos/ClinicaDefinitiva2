package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class SchedulingException extends AppointmentBusinessRuleViolationException{
    public SchedulingException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
