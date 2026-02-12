package com.example.ClinicaDefinitiva.domain.actor.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Specialties {
    private final Set<Specialty> values;

    private Specialties(Set<Specialty> values) {
        if (values == null || values.isEmpty()){
            throw new ValueObjectValidationException(VoActorError.ERR_DENTIST_INVALID_SPECIALTY, VOContext.SPECIALTY  );
        }

        this.values = Collections.unmodifiableSet(new HashSet<>(values));
    }
    public static Specialties of(Set<Specialty> values){return new Specialties(values);}

    // methods semantic
    public boolean contains(Specialty specialty) {
        return values.contains(specialty);
    }

    public boolean isMultidisciplinary() {
        return values.size() > 1;
    }

    public boolean allowsSurgicalProcedures() {
        return contains( Specialty.of("Oral Surgery"));
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
