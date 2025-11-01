package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class  DentistId {
    private final String value;

    public DentistId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public static DentistId generate (){
        return new DentistId(UUID.randomUUID().toString());
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static DentistId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceId string is empty");
        return new DentistId(trimmed);
    }



    public String getValue() {
        return value;
    }
}
