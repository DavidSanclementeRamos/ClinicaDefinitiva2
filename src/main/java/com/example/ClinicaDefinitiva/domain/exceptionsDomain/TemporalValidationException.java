package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

/**
 * Excepción lanzada cuando un rango temporal o condición cronológica
 * es inválida dentro del dominio.
 * * Extiende {@link ModelException}
 */
public class TemporalValidationException extends ModelException {
    public TemporalValidationException(ErrorCatalog catalogo, ContextoEntidad contexto ) {
        super(catalogo, contexto);
    }
}
