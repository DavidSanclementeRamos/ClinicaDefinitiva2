package com.example.ClinicaDefinitiva.domain.exceptions.receptionist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class BlankSectorException extends ReceptionistBusinessRuleViolationException {
    public BlankSectorException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.BLANK_SECTOR, contexto, detalle);
    }
}
