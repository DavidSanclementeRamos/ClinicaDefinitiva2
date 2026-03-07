package com.example.ClinicaDefinitiva.domain.exceptions;


import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;

/**
 * Excepción lanzada cuando un {ValueObject} no cumple con las reglas de validación
 * definidas en el dominio.
 * * Extiende {@link ModelException}
 */
public class ValueObjectValidationException extends ModelException {


    public ValueObjectValidationException(ErrorCatalog catalogo, VOContext contexto){
        super(catalogo, contexto);
    }


}
