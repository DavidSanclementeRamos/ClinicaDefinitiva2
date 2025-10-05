package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class UserNotFoundException extends ClinicaDefinitivaException {
  public UserNotFoundException(ContextoEntidad contexto, String detalle ) {

    super(ErrorCatalog.USER_NOT_FOUND, contexto, detalle);
  }
}
