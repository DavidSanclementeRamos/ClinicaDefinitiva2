package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;



/**
 * Excepción de dominio que representa la violación de una regla de negocio
 * dentro de un agregado o contexto específico.
 * Extiende {@link ModelException}
 * Se utiliza cuando una operación incumple una restricción explícita
 * definida por el modelo de negocio.
 */
public class BusinessRuleViolationException extends ModelException {
    public BusinessRuleViolationException(ErrorCatalog catalogo, DomainContext contexto ) {
        super(catalogo, contexto);
    }
}
