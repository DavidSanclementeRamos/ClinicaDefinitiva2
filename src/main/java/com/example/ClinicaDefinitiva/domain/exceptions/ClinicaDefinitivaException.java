package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;

public class ClinicaDefinitivaException extends RuntimeException {

  private final ErrorCatalog catalogo;
  private final ContextoEntidad contexto;
  private final String requestId;

  public ClinicaDefinitivaException(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
    super(detalle);
    this.catalogo = catalogo;
    this.contexto = contexto;
    this.requestId = RequestIdFilter.getRequestId();  // se asigna automáticamente
  }

  public String getRequestId() {
    return requestId;
  }

  public ErrorCatalog getCatalogo() {
    return catalogo;
  }

  public ContextoEntidad getContexto() {
    return contexto;
  }
}
