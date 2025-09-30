package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;

public final class FullName {
    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
        if (isBlank(firstName) || isBlank(lastName)) {
            throw new IllegalArgumentException("First name and last name must not be empty.");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

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
