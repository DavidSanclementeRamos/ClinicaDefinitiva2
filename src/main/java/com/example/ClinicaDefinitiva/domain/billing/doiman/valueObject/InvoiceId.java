package com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class InvoiceId {
    private final String value;

    public InvoiceId(String value) {
        this.value = Objects.requireNonNull(value, "InvoiceId value cannot be null");
        if (value.trim().isEmpty()) throw new IllegalArgumentException("InvoiceId value cannot be empty");
    }

    public static InvoiceId generate() {
        return new InvoiceId(UUID.randomUUID().toString());
    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static InvoiceId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceId string is empty");
        return new InvoiceId(trimmed);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceId)) return false;
        InvoiceId that = (InvoiceId) o;
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
