package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class UsuarioNotfoundException extends ClinicaDefinitivaException {
  public UsuarioNotfoundException(ContextoEntidad contexto, String detalle ) {

    super(CatalogoError.USER_NOT_FOUND, contexto, detalle);
  }
}
