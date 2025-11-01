package com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject;

import java.util.UUID;

public final class InvoiceItemId {
    private final String value;

    public InvoiceItemId(String value) {
        this.value = value;
    }
    public static InvoiceItemId generate(){
        return new InvoiceItemId(UUID.randomUUID().toString());
    }
    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static InvoiceItemId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceItemId string is empty");
        return new InvoiceItemId(trimmed);
    }
    public String getValue() {
        return value;
    }
}
