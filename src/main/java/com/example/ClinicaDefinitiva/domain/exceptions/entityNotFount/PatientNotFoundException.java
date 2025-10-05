package com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.ClinicaDefinitivaException;

public class PatientNotFoundException extends ClinicaDefinitivaException {
  public PatientNotFoundException(ContextoEntidad contexto, String detalle )
  {
    super(ErrorCatalog.PATIENT_NOT_FOUND, contexto, detalle);
  }
}
