package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

/**
 * Excepción lanzada cuando una regla de negocio definida en un agregado
 * es violada.
 * * Extiende {@link ModelException}
 */
public class DomainAggregateException extends ModelException {
    public DomainAggregateException(ErrorCatalog catalogo, ContextoEntidad contexto ) {
        super(catalogo, contexto);
    }
}
