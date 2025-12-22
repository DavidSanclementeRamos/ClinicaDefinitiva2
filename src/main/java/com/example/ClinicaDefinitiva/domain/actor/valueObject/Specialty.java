package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;

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

    public  Specialty(String value) {
       /* if (isBlank(value)) {
            throw new IllegalArgumentException("Specialty must not be empty.");
        }*/
        String normalized = value.trim();
        if (!VALID_SPECIALTIES.contains(normalized)) {
            throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_INVALID_SPECIALTY,ContextoEntidad.DENTIST );
        }
        this.value = normalized;
    }

   // public Specialty(String value) {
     //   this.value = value;
   // }

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
