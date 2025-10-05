package com.example.ClinicaDefinitiva.domain.exceptions.user.exception;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;

public class NullUserException extends UserBusinessRuleViolationException {
  public NullUserException(ContextoEntidad contexto, String detalle) {
    super(ErrorCatalog.NULL_USER, contexto, detalle);
  }
}
