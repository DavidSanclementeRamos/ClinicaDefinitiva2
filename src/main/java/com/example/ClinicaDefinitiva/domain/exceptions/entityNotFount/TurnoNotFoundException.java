package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class TurnoNotFoundException extends ClinicaDefinitivaException {
  public TurnoNotFoundException(ContextoEntidad contexto, String detalle) {

    super(ErrorCatalog.SHIFT_NOT_FOUND, contexto, detalle);
  }
}
