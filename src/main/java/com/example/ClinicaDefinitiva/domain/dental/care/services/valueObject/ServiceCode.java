package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;

public final class ServiceCode {
    private final String value;

    public ServiceCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ServiceCode cannot be null or blank");
        }
        if (!value.matches("^[A-Z0-9\\-]+$")) { // Ejemplo: solo mayúsculas, números y guiones
            throw new IllegalArgumentException("Invalid ServiceCode format: " + value);
        }
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public String toString() { return value; }


}
