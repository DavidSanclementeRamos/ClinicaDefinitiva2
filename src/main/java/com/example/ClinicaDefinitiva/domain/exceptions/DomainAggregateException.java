package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;

/**
 * Excepción lanzada cuando una regla de negocio definida en un agregado
 * es violada.
 * * Extiende {@link ModelException}
 */
public class DomainAggregateException extends ModelException {
    public DomainAggregateException(ErrorCatalog catalogo, DomainContext contexto ) {
        super(catalogo, contexto);
    }
}
