package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class PatientId {
    private final String value;

    public PatientId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static PatientId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceId string is empty");
        return new PatientId(trimmed);
    }


    public String getValue() {
        return value;
    }
}
