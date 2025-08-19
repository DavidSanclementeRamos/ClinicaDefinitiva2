package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class SecretarioNotFoundException extends ClinicaDefinitivaException {
  public SecretarioNotFoundException(ContextoEntidad contexto, String detalle) {

    super(CatalogoError.SECRETARY_NOT_FOUND, contexto, detalle);
  }
}
