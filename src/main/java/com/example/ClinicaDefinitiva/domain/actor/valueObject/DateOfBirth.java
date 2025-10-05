package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.DateOfBirthInFutureException;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.invalid.InvalidDateOfBirthException;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.nulo.NullDateOfBirthException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public final class DateOfBirth {
    private final LocalDate value;

    public DateOfBirth(LocalDate value) {
        if (value == null) {
            throw new NullDateOfBirthException(ContextoEntidad.DATE_OF_BIRTH, "Date of birth cannot be null.");
        }

        if (value.isAfter(LocalDate.now())) {
            throw new DateOfBirthInFutureException(ContextoEntidad.DATE_OF_BIRTH, "Date of birth cannot be in the future.");
        }
        if (Period.between(value, LocalDate.now()).getYears() > 130) {
            throw new InvalidDateOfBirthException(ContextoEntidad.DATE_OF_BIRTH, "Date of birth is not valid: age exceeds 130 years.");
        }
        this.value = value;
    }

    // methods semantic
    public LocalDate asDate() {
        return value;
    }

    // methods access
    public LocalDate Value() {
        return value;
    }

    // methods utility
    @Override
    public String toString() {
        return "Date of birth: " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateOfBirth)) return false;
        DateOfBirth that = (DateOfBirth) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


}
