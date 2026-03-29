package com.example.ClinicaDefinitiva.domain.actor.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public final class Sector {

    public enum Type {
        RECEPTION("Recepción"),
        ADMINISTRATION("Administración"),
        BILLING("Facturación"),
        CUSTOMER_SERVICE("Atención al cliente"),
        MEDICAL_RECORDS("Historias clínicas"),
        CALL_CENTER("Call Center"),
        INVENTORY("Inventario"),
        DENTAL_TECHNICIAN_SUPPORT("Soporte técnico odontológico"),
        HUMAN_RESOURCES("Recursos Humanos"),

        DENTAL_ASSISTANCE("Asistencia odontológica");

        private final String description;

        Type(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    private final Type value;

    private Sector(Type value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_SECTOR_NULL, VOContext.ACTORS);
        }
        this.value = value;
    }

    public static Sector of(Type value) { return new Sector(value); }

    // Nuevo: conversión desde String para DTOs
    public static Sector fromString(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new ValueObjectValidationException(VoActorError.ERR_SECTOR_BLANK, VOContext.ACTORS);
        }
        try {
            return of(Type.valueOf(raw.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ValueObjectValidationException(VoActorError.ERR_SECTOR_NOT_ALLOWED, VOContext.ACTORS);
        }
    }

    // Queries semánticas
    public boolean is(Type expected) { return value == expected; }

    // Access
    public Type getValue() { return value; }
    public String getDescription() { return value.getDescription(); }

    @Override
    public String toString() { return value.name(); }
}