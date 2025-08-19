package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class HorarioNotfoundException extends ClinicaDefinitivaException {
  public HorarioNotfoundException(ContextoEntidad contexto, String detalle) {

    super(CatalogoError.SCHEDULE_NOT_FOUND, contexto, detalle);
  }
}
