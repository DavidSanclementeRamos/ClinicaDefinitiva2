package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.VOContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;

/**
 * Excepción base para errores relacionados con el modelo de dominio.
 * Extiende {@link ClinicaDefinitivaException} y se utiliza para representar
 * inconsistencias o violaciones dentro de la capa de modelo (Value Objects,
 * entidades y agregados)
 */
public class ModelException extends ClinicaDefinitivaException{

    public ModelException(ErrorCatalog catalogo, VOContext contexto) {
        super(catalogo, contexto);
    }
}
