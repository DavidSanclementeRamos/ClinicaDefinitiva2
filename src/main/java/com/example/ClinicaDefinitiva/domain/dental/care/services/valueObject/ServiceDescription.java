package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;


import java.util.Objects;

/**
 * Value Object para la descripción del servicio odontológico.
 * Garantiza longitud mínima y semántica clara.
 */
public final class ServiceDescription {

    public final String description;

    private ServiceDescription(String description) {
        if (description == null || description.trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres");
        }
        this.description = description.trim();
    }

    public static ServiceDescription of(String description) {
        return new ServiceDescription(description);
    }

    public String getValue() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceDescription)) return false;
        ServiceDescription that = (ServiceDescription) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return description;
    }
}