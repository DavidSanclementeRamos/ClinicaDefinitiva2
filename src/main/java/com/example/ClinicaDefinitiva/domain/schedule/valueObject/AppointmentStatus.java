package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

public final class AppointmentStatus {
    public enum Status {
        SCHEDULED,
        COMPLETED,
        CANCELLED,
        CONFIRMED,
        NO_SHOW
    }

    private final Status value;

    private AppointmentStatus(Status value) {
        this.value = value;
    }

    public static AppointmentStatus from(Status value) {
        if (value == null) {
            throw new IllegalArgumentException("Appointment status cannot be null.");
        }
        return new AppointmentStatus(value);
    }

    public Status getValue() {
        return value;
    }

    public boolean isScheduled() {
        return value == Status.SCHEDULED;
    }

    public boolean isCompleted() {
        return value == Status.COMPLETED;
    }

    public boolean isCancelled() {
        return value == Status.CANCELLED;
    }
    public boolean isConfirmed() {
        return value == Status.CONFIRMED;
    }

    public boolean isNoShow() {
        return value == Status.NO_SHOW;
    }

    @Override
    public String toString() {
        return value.name();
    }




}
