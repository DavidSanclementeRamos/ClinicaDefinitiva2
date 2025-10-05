package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class WeeklyAvailabilityException extends ClinicaDefinitivaException {

    public WeeklyAvailabilityException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.INVALID_WEEKLY_AVAILABILITY, contexto, detalle);
    }
}
