package com.example.ClinicaDefinitiva.domain.exceptions.receptionist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class NullSectorException extends ReceptionistBusinessRuleViolationException {
    public NullSectorException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NULL_SECTOR, contexto, detalle);
    }
}
