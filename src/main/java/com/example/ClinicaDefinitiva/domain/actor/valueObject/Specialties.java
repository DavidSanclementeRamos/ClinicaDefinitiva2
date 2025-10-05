package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception.EmptySpecialtySetException;
import com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception.NullSpecialtySetException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Specialties {
    private final Set<Specialty> values;

    public Specialties(Set<Specialty> values) {
        if (values == null ){
            throw new NullSpecialtySetException(ContextoEntidad.DENTIST, "At least one specialty must be null.");
        }
        if (values.isEmpty()){
            throw new EmptySpecialtySetException(ContextoEntidad.DENTIST, "At least one specialty must be provided.");
        }
        this.values = Collections.unmodifiableSet(new HashSet<>(values));
    }

    // methods semantic
    public boolean contains(Specialty specialty) {
        return values.contains(specialty);
    }

    public boolean isMultidisciplinary() {
        return values.size() > 1;
    }

    public boolean allowsSurgicalProcedures() {
        return contains(new Specialty("Oral Surgery"));
    }

    public Set<Specialty> asSet() {
        return values;
    }

    // methods access
    public Set<Specialty> Values() {
        return values;
    }

    // methods utility
    @Override
    public String toString() {
        return "Specialties: " + values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specialties)) return false;
        Specialties that = (Specialties) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }




}
