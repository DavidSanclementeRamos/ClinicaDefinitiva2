package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class NoShiftAssignedException extends SchedulingException{
    public NoShiftAssignedException( ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_SHIFT, contexto, detalle);
    }
}
