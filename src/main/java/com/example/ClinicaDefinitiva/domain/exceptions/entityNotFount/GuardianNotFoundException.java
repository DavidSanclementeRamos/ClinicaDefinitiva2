package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class GuardianNotFoundException extends ClinicaDefinitivaException {
  public GuardianNotFoundException(ContextoEntidad contexto, String detalle) {

    super(ErrorCatalog.RESPONSIBLE_NOT_FOUND, contexto, detalle);
  }
}
