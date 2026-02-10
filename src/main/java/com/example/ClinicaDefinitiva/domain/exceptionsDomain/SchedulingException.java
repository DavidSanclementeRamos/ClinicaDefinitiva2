package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
//import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;

/**
 * Excepción lanzada cuando ocurre un conflicto en la programación
 * o asignación de recursos (ej. turnos, citas).
 * * Extiende {@link ModelException}
 */
public class SchedulingException extends ModelException {
    public SchedulingException(ErrorCatalog catalogo, EntityContext contexto ) {
        super(catalogo, contexto);
    }
}
