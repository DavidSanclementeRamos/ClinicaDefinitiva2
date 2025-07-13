package com.example.ClinicaDefinitiva.exceptions;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;

public class ClinicaDefinitivaException extends RuntimeException {

  private final CatalogoError catalogo;
  private final ContextoEntidad contexto;
  private final String requestId;

  public ClinicaDefinitivaException(CatalogoError catalogo, ContextoEntidad contexto, String detalle) {
    super(detalle);
    this.catalogo = catalogo;
    this.contexto = contexto;
    this.requestId = RequestIdFilter.getRequestId();  // se asigna automáticamente
  }

  public String getRequestId() {
    return requestId;
  }

  public CatalogoError getCatalogo() {
    return catalogo;
  }

  public ContextoEntidad getContexto() {
    return contexto;
  }
}
