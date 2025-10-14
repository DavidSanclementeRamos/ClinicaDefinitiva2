package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;

public final class ServiceStatus {
    private final String value;

    private static final String[] ALLOWED = {"Active", "Inactive", "Deprecated"};

    public ServiceStatus(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ServiceStatus cannot be null or blank");
        }
        boolean valid = false;
        for (String allowed : ALLOWED) {
            if (allowed.equals(value)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            throw new IllegalArgumentException("Invalid ServiceStatus: " + value);
        }
        this.value = value;
    }

    public boolean isActive() {
        return "Active".equals(this.value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }



}
