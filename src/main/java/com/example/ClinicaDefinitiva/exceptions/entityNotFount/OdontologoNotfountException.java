package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class OdontologoNotfountException extends ClinicaDefinitivaException {
    public OdontologoNotfountException(ContextoEntidad contexto, String detalle) {

        super(CatalogoError.DENTIST_NOT_FOUND, contexto, detalle);
    }


}
