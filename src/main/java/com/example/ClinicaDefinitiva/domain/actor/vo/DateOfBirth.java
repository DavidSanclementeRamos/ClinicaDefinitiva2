package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public final class DateOfBirth {
    private final LocalDate value;

    private DateOfBirth(LocalDate value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_BIRTHDATE_NULL, VOContext.DATE_OF_BIRTH);
        }

        if (value.isAfter(LocalDate.now())) {
            throw new ValueObjectValidationException(VoActorError.ERR_BIRTHDATE_FUTURE, VOContext.DATE_OF_BIRTH);
        }
        if (Period.between(value, LocalDate.now()).getYears() > 130) {
            throw new ValueObjectValidationException(VoActorError.ERR_BIRTHDATE_INVALID_RANGE, VOContext.DATE_OF_BIRTH);
        }
        this.value = value;
    }
    public static DateOfBirth of(LocalDate value) { return new DateOfBirth(value); }

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
