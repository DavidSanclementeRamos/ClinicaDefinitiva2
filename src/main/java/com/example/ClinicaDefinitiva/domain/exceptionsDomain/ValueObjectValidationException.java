package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

/**
 * Excepción lanzada cuando un {ValueObject} no cumple con las reglas de validación
 * definidas en el dominio.
 * * Extiende {@link ModelException}
 */
public class ValueObjectValidationException extends ModelException {


    public ValueObjectValidationException(ErrorCatalog catalogo, ContextoEntidad contexto){
        super(catalogo, contexto);
    }


}
