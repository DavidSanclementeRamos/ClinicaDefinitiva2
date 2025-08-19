package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class TurnoNotFoundException extends ClinicaDefinitivaException {
  public TurnoNotFoundException(ContextoEntidad contexto, String detalle) {

    super(CatalogoError.SHIFT_NOT_FOUND, contexto, detalle);
  }
}
