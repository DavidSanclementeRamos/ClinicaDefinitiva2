package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class ReceptionId {
private final String value;

    public ReceptionId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static ReceptionId generate(){
        return new ReceptionId(UUID.randomUUID().toString());
    }

    public static ReceptionId fromString(String value) {
        if (value == null) return null; // puedes lanzar excepción si prefieres forzar presencia
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("ReceptionId string is empty");
        }
        return new ReceptionId(trimmed);
    }
    public String getValue() {
        return value;
    }
}
