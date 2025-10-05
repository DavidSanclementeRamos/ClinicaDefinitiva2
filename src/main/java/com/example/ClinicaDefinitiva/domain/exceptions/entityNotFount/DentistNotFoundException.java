package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class DentistNotFoundException extends ClinicaDefinitivaException {
    public DentistNotFoundException(ContextoEntidad contexto, String detalle) {

        super(ErrorCatalog.DENTIST_NOT_FOUND, contexto, detalle);
    }


}
