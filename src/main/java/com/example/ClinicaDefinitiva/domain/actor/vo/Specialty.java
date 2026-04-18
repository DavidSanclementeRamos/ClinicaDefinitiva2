package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public enum Specialty {
    ORTHODONTICS("Ortodoncia"),
    ENDODONTICS("Endodoncia"),
    PERIODONTICS("Periodoncia"),
    PROSTHODONTICS("Prótesis dental"),
    PEDIATRIC_DENTISTRY("Odontopediatría"),
    ORAL_SURGERY("Cirugía oral"),
    GENERAL_DENTISTRY("Odontología general");

    private final String displayName;

    Specialty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return this.name();
    }

    /**
     * Convierte un string (case-insensitive) en la constante del enum correspondiente.
     * Acepta tanto el nombre técnico (ej. "ORTHODONTICS") como la descripción (ej. "Ortodoncia").
     */
    public static Specialty fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(VoActorError.ERR_DENTIST_INVALID_SPECIALTY, VOContext.ACTORS);
        }
        String trimmed = value.trim();
        // Buscar por nombre técnico (case-insensitive)
        for (Specialty spec : values()) {
            if (spec.name().equalsIgnoreCase(trimmed) || spec.name().replace("_", " ").equalsIgnoreCase(trimmed)) {
                return spec;
            }
        }
        // Buscar por displayName (case-insensitive)
        for (Specialty spec : values()) {
            if (spec.getDisplayName().equalsIgnoreCase(trimmed)) {
                return spec;
            }
        }
        throw new ValueObjectValidationException(VoActorError.ERR_DENTIST_INVALID_SPECIALTY, VOContext.ACTORS);
    }

    @Override
    public String toString() {
        return this.name();
    }
}
