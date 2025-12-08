package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

/**
 * Excepción lanzada cuando ocurre un conflicto en la programación
 * o asignación de recursos (ej. turnos, citas).
 * * Extiende {@link ModelException}
 */
public class SchedulingException extends ModelException {
    public SchedulingException(ErrorCatalog catalogo, ContextoEntidad contexto ) {
        super(catalogo, contexto);
    }
}
