package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import java.time.LocalDate;
import java.time.Period;

public final class Age {

    private final int value;

    private Age(DateOfBirth dateOfBirth) {
        this.value = Period.between(dateOfBirth.asDate(), LocalDate.now()).getYears();
        if (value < 0 || value > 130) {
            throw new ValueObjectValidationException(VoActorError.ERR_AGE_OUT_OF_RANGE, VOContext.ACTORS);
        }
    }

    public static Age of(DateOfBirth dateOfBirth) { return new Age(dateOfBirth); }

    // methods semantice
    public boolean isAdult() {
        return value >= 18;
    }

    public boolean isElderly() {
        return value >= 65;
    }

    public boolean isEligibleForRegistration() {
        return value >= 13;
    }

    public boolean isBetween(int min, int max) {
        return value >= min && value <= max;
    }

    public String ageCategory() {
        if (value < 13) return "Child";
        if (value < 18) return "Teenager";
        if (value < 65) return "Adult";
        return "Senior";
    }

    public int asInt() {
        return value;
    }

    // methods access
    public int Value() {
        return value;
    }

    // methods utility
    @Override
    public String toString() {
        return "Age: " + value + " (" + ageCategory() + ")";
    }



}
