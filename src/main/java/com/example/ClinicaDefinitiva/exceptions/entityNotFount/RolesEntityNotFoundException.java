package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class RolesEntityNotFoundException extends ClinicaDefinitivaException {

   public RolesEntityNotFoundException (ContextoEntidad contexto, String detalle) {
        super(CatalogoError.ROLE_NOT_FOUND, contexto, detalle);
    }
}
