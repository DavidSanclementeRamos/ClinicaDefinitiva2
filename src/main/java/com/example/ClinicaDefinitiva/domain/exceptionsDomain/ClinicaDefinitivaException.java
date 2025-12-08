package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;

/**
 * Excepción raíz del sistema clínico-administrativo.
 * Representa cualquier error controlado dentro del dominio de la aplicación,
 * proporcionando trazabilidad mediante un catálogo de errores, el contexto de la entidad
 * afectada y un identificador único de la petición.
 */
public class ClinicaDefinitivaException extends RuntimeException {

  private final ErrorCatalog catalogo;
  private final ContextoEntidad contexto;
  private final String requestId;


  public ClinicaDefinitivaException(ErrorCatalog catalogo, ContextoEntidad contexto) {
    super(catalogo.getMessage()); // fallback
    this.catalogo = catalogo;
    this.contexto = contexto;
    this.requestId = RequestIdFilter.getRequestId();
  }

  public String getRequestId() { return requestId; }
  public ErrorCatalog getCatalogo() { return catalogo; }
  public ContextoEntidad getContexto() { return contexto; }


}
