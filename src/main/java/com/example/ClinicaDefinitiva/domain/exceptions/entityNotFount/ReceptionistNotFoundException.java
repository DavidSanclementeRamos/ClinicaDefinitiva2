package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class ReceptionistNotFoundException extends ClinicaDefinitivaException {
  public ReceptionistNotFoundException(ContextoEntidad contexto, String detalle) {

    super(ErrorCatalog.SECRETARY_NOT_FOUND, contexto, detalle);
  }
}
