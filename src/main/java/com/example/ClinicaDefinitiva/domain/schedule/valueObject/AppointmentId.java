package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;

import java.util.Objects;
import java.util.UUID;

public final class AppointmentId {
    private final String value;

    public AppointmentId(String value) {
        this.value = Objects.requireNonNull(value, "AppointmentId value cannot be null");
        if (value.trim().isEmpty()) throw new IllegalArgumentException("AppointmentId value cannot be empty");
    }

    public static AppointmentId generate() {
        return new AppointmentId(UUID.randomUUID().toString());
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static AppointmentId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("AppointmentId string is empty");
        return new AppointmentId(trimmed);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentId)) return false;
        AppointmentId that = (AppointmentId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }


}
