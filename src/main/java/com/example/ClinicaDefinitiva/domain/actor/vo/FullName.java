package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class FullName {
    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        if(firstName == null || lastName == null){
            throw new ValueObjectValidationException(VoActorError.ERR_FULLNAME_NULL, VOContext.FULL_NAME);
        }
        if (isBlank(firstName) || isBlank(lastName)) {
            throw new ValueObjectValidationException(VoActorError.ERR_FULLNAME_BLANK, VOContext.FULL_NAME);
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    public static FullName of(String firstName, String lastName){return new FullName(firstName,lastName);}

    // methods semantic
    public String asText() {
        return firstName + " " + lastName;
    }

    public boolean matches(String fullNameCandidate) {
        return asText().equalsIgnoreCase(fullNameCandidate.trim());
    }

    public boolean startsWith(String prefix) {
        return asText().toLowerCase().startsWith(prefix.toLowerCase().trim());
    }

    public String initials() {
        return (firstName.charAt(0) + "" + lastName.charAt(0)).toUpperCase();
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // methods access
    public String LastName() {
        return lastName;
    }

    public String FirstName() {
        return firstName;
    }

    // methods utility
    @Override
    public String toString() {
        return asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName)) return false;
        FullName fullName = (FullName) o;
        return firstName.equalsIgnoreCase(fullName.firstName) &&
                lastName.equalsIgnoreCase(fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase(), lastName.toLowerCase());
    }



}
