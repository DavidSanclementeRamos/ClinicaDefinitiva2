package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class HorarioNotfoundException extends ClinicaDefinitivaException {
  public HorarioNotfoundException(ContextoEntidad contexto, String detalle) {

    super(ErrorCatalog.SCHEDULE_NOT_FOUND, contexto, detalle);
  }
}
