package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;

/**
 * Excepción lanzada cuando un rango temporal o condición cronológica
 * es inválida dentro del dominio.
 * * Extiende {@link ModelException}
 */
public class TemporalValidationException extends ModelException {
    public TemporalValidationException(ErrorCatalog catalogo, EntityContext contexto ) {
        super(catalogo, contexto);
    }
}
