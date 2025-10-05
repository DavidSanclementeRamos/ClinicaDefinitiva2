package com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;


/**
 * Se lanza cuando la edad de un odontólogo no cumple con el mínimo requerido para ejercer.
 * Exhibe la regla clínica de legitimidad profesional: la edad debe estar entre 25 y 130 años.
 */
public class DentistMinimumAgeException extends DentistBusinessRuleViolationException {
    public DentistMinimumAgeException(ContextoEntidad contexto, String detalle) {
        super(ErrorCatalog.MinimumAge_Dentist, contexto, detalle);
    }
}
