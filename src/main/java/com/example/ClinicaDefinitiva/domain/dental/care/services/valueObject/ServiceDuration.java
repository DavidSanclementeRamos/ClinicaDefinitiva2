package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;

public final class ServiceDuration {
    private final int minutes;

    public ServiceDuration(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("ServiceDuration must be greater than 0 minutes");
        }
        this.minutes = minutes;
    }

    public int getMinutes() { return minutes; }

    @Override
    public String toString() {
        return minutes + " minutes";
    }


}
