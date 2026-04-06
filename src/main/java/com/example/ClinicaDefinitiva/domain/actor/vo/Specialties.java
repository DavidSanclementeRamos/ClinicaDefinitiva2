package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class Specialties {
    private final Set<Specialty> specialties;

    private Specialties(Set<Specialty> specialties) {
        if (specialties == null || specialties.isEmpty()) {
            throw new ValueObjectValidationException(VoActorError.ERR_SERVICE_EMPTY_SPECIALTIES, VOContext.ACTORS);
        }
        this.specialties = Collections.unmodifiableSet(new HashSet<>(specialties));
    }

    public static Specialties of(Set<Specialty> specialties) {
        return new Specialties(specialties);
    }

    public Set<Specialty> asSet() {
        return specialties;
    }

    public boolean contains(Specialty specialty) {
        return specialties.contains(specialty);
    }

    public boolean isMultidisciplinary() {
        return specialties.size() > 1;
    }

    public boolean allowsSurgicalProcedures() {
        return specialties.contains(Specialty.ORAL_SURGERY);
    }

    public static Specialties fromString(String concatenated) {
        if (concatenated == null || concatenated.isBlank()) {
            throw new ValueObjectValidationException(VoActorError.ERR_SERVICE_EMPTY_SPECIALTIES, VOContext.ACTORS);
        }
        Set<Specialty> set = java.util.Arrays.stream(concatenated.split(","))
                .map(String::trim)
                .map(Specialty::fromString)
                .collect(Collectors.toSet());
        return new Specialties(set);
    }

    @Override
    public String toString() {
        return specialties.stream()
                .map(Specialty::getCode)
                .collect(Collectors.joining(","));
    }
}