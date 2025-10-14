package com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class ShiftNotAvailableException extends SchedulingException{
    public ShiftNotAvailableException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_SHIFT, contexto, detalle);
    }
}
