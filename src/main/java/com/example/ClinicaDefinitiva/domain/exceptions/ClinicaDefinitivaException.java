package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;

public class ClinicaDefinitivaException extends RuntimeException {

  private final ErrorCatalog catalogo;
  private final ContextoEntidad contexto;
  private final String requestId;
  private String params;

  public ClinicaDefinitivaException(ErrorCatalog catalogo, ContextoEntidad contexto, String params) {
    super(catalogo.getMessage()); // fallback
    this.catalogo = catalogo;
    this.contexto = contexto;
    this.params = params;
    this.requestId = RequestIdFilter.getRequestId();
  }

  public String getRequestId() { return requestId; }
  public ErrorCatalog getCatalogo() { return catalogo; }
  public ContextoEntidad getContexto() { return contexto; }
  public String getParams() { return params; }


}
