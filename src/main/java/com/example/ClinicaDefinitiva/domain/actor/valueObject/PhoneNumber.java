package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class PhoneNumber {
    private static final Pattern VALID_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

    private final String value;

    public PhoneNumber(String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Phone number must not be empty.");
        }
        String normalized = value.trim().replaceAll("\\s+", "");
        if (!VALID_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
        this.value = normalized;
    }

    // methods semantic
    public boolean isInternational() {
        return value.startsWith("+");
    }

    public boolean isLocalTo(String countryCode) {
        return value.startsWith(countryCode);
    }

    public String masked() {
        int visibleDigits = Math.min(4, value.length());
        return "***" + value.substring(value.length() - visibleDigits);
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
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber that = (PhoneNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }



}
