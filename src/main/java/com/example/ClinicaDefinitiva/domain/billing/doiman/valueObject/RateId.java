package com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class RateId {
    private final String value;

    public RateId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public RateId generate(){
        return new RateId(UUID.randomUUID().toString());
    }
    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static RateId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceId string is empty");
        return new RateId(trimmed);
    }
    public String getValue() {
        return value;
    }
}
