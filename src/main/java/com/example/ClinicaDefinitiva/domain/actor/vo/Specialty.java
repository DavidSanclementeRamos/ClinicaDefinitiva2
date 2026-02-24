package com.example.ClinicaDefinitiva.domain.actor.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.Set;

public final class Specialty {
    private static final Set<String> VALID_SPECIALTIES = Set.of(
            "Orthodontics",
            "Endodontics",
            "Periodontics",
            "Prosthodontics",
            "Pediatric Dentistry",
            "Oral Surgery",
            "General Dentistry"
    );

    private final String value;

    private   Specialty(String value) {
        if (isBlank(value)) {
            throw new ValueObjectValidationException(VoActorError.ERR_DENTIST_INVALID_SPECIALTY,VOContext.ACTORS);
        }
        String normalized = value.trim();
        if (VALID_SPECIALTIES.stream().noneMatch(s -> s.equalsIgnoreCase(normalized))) {
          throw new ValueObjectValidationException(VoActorError.ERR_DENTIST_INVALID_SPECIALTY, VOContext.ACTORS);

        }
        this.value = normalized;
    }


    public static Specialty of(String value){return new Specialty(value);}

    // methods semantic
    public boolean is(String expected) {
        return value.equalsIgnoreCase(expected.trim());
    }

    public String asText() {
        return value;
    }

    private boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    // methods access
    public String Value() {
        return value;
    }

    // methods utility
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specialty)) return false;
        Specialty that = (Specialty) o;
        return value.equalsIgnoreCase(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }





}
