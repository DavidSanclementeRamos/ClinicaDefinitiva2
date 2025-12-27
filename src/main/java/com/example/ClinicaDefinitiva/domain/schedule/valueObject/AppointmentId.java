package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import java.util.Objects;

public final class AppointmentId {
    private final String value;

    public AppointmentId(String value) {
        this.value = Objects.requireNonNull(value, "AppointmentId value cannot be null");
        if (value.trim().isEmpty()) throw new IllegalArgumentException("AppointmentId value cannot be empty");
    }


    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static AppointmentId fromString(String value) {
        if (value == null) throw new IllegalArgumentException("AppointmentId value cannot be empty");
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
