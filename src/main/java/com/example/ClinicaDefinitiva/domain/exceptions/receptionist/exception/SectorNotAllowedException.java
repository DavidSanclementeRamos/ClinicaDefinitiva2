package com.example.ClinicaDefinitiva.domain.exceptions.receptionist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

/**
 * Thrown when a Value Object receives a sector value that is not part of the allowed domain.
 * Exhibits semantic and ethical boundaries of sector legitimacy.
 */

public class SectorNotAllowedException extends ReceptionistBusinessRuleViolationException {
    public SectorNotAllowedException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.NOT_ALLOWED_SECTOR, contexto, detalle);
    }
}
