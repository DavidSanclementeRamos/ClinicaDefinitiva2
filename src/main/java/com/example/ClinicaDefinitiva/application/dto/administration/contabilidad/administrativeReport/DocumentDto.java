package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.fasterxml.jackson.databind.util.Named;

public record DocumentDto(NameDto name, String url, String type, long size) {
}
