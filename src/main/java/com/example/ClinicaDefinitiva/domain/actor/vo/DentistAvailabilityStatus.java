package com.example.ClinicaDefinitiva.domain.actor.vo;





import java.util.Objects;

public final class DentistAvailabilityStatus {

    public enum Status {
        AVAILABLE,      // Disponible para atender
        SICK_LEAVE,     // Incapacidad médica
        VACATION        // Ausencia planificada
    }

    private final Status current;

    private DentistAvailabilityStatus(Status current) {
        this.current = Objects.requireNonNull(current, "Status cannot be null");
    }

    public static DentistAvailabilityStatus of(Status status) {
        return new DentistAvailabilityStatus(status);
    }

    public Status getCurrent() {
        return current;
    }

    public boolean isAvailable() {
        return current == Status.AVAILABLE;
    }

    public boolean isAbsent() {
        return current == Status.SICK_LEAVE || current == Status.VACATION;
    }

    public enum Priority { NOT_ASSIGNABLE, HIGH }

    public Priority getPriority() {
        return switch (current) {
            case AVAILABLE -> Priority.HIGH;
            default -> Priority.NOT_ASSIGNABLE;
        };
    }

    @Override
    public String toString() {
        return current.name();
    }
}
