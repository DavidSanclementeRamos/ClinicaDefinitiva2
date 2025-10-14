package com.example.ClinicaDefinitiva.domain.exceptions.patient.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

public class UnassignedResponsibleException extends PatientBusinessRuleViolationException{
    public UnassignedResponsibleException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.UNASSIGNED_RESPONSIBLE_PATIENT, contexto, detalle);
    }
}
