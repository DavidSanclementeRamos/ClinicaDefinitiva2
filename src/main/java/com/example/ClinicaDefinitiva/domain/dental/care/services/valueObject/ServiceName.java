package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;


import java.util.Objects;

/**
 * Value Object para el nombre del servicio odontológico.
 * Incluye un enum con valores estáticos y la posibilidad de ingresar otro nombre.
 */
public final class ServiceName {

    public enum DentalServiceName {
        CLEANING,
        WHITENING,
        ORTHODONTICS,
        IMPLANTOLOGY,
        PROSTHETICS,
        PEDIATRICS,
        SURGERY,
        OTHER // Permite ingresar un nombre personalizado
    }

    private final DentalServiceName predefinedName;
    private final String customName;

    private ServiceName(DentalServiceName predefinedName, String customName) {
        this.predefinedName = predefinedName;
        this.customName = customName;
    }

    public static ServiceName of(DentalServiceName predefinedName) {
        return new ServiceName(predefinedName, null);
    }

    public static ServiceName custom(String customName) {
        if (customName == null || customName.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre personalizado debe tener al menos 3 caracteres");
        }
        return new ServiceName(DentalServiceName.OTHER, customName.trim());
    }

    public String getValue() {
        return predefinedName == DentalServiceName.OTHER ? customName : predefinedName.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceName)) return false;
        ServiceName that = (ServiceName) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return getValue();
    }
}
