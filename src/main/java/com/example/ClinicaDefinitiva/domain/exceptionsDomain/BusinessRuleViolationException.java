package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import org.springframework.data.repository.query.Param;


/**
 * Excepción de dominio que representa la violación de una regla de negocio
 * dentro de un agregado o contexto específico.
 * Extiende {@link ModelException}
 * Se utiliza cuando una operación incumple una restricción explícita
 * definida por el modelo de negocio.
 */
public class BusinessRuleViolationException extends ModelException {
    public BusinessRuleViolationException(ErrorCatalog catalogo, ContextoEntidad contexto ) {
        super(catalogo, contexto);
    }
}
