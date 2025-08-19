package com.example.ClinicaDefinitiva.exceptions.entityNotFount;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.exceptions.ClinicaDefinitivaException;

public class PacienteNotFoundException extends ClinicaDefinitivaException {
  public PacienteNotFoundException(ContextoEntidad contexto, String detalle )
  {
    super(CatalogoError.PATIENT_NOT_FOUND, contexto, detalle);
  }
}
