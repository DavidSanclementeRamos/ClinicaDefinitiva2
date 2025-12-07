package com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject;

import java.util.Objects;

/**
 * Value Object que representa el ID de un reporte administrativo.
 * Inmutable y con validaciones de negocio.
 */
public class AdministrativeReportId {
    private final String value;

    public AdministrativeReportId(String value) {
        this.value = Objects.requireNonNull(value, " AdministrativeReportId value cannot be null");
    }

    /**
     * Parsea/validad una cadena y devuelve el VO.
     */
    public static AdministrativeReportId fromString(String value) {
        if (value == null) throw new IllegalArgumentException("AdministrativeReportId string is null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("AdministrativeReportId string is empty");
        return new AdministrativeReportId(trimmed);
    }


    public String getValue() {
        return value;
    }

}