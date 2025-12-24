package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.DomainContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;

/**
 * Excepción raíz del sistema clínico-administrativo.
 * Representa cualquier error controlado dentro del dominio de la aplicación,
 * proporcionando trazabilidad mediante un catálogo de errores, el contexto de la entidad
 * afectada y un identificador único de la petición.
 */
public class ClinicaDefinitivaException extends RuntimeException {

  private final ErrorCatalog catalogo;
  private final DomainContext contexto;
  private final String requestId;


  public ClinicaDefinitivaException(ErrorCatalog catalogo, DomainContext contexto) {
    super(catalogo.getMessage()); // fallback
    this.catalogo = catalogo;
    this.contexto = contexto;
    this.requestId = RequestIdFilter.getRequestId();
  }

  public String getRequestId() { return requestId; }
  public ErrorCatalog getCatalogo() { return catalogo; }
  public DomainContext getContexto() { return contexto; }


}
