package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class ShiftStatus {
    public enum Status {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

    private Status value;

    private static final EnumMap<Status, Set<Status>> VALID_TRANSITIONS = new EnumMap<>(Status.class);

    static {
        VALID_TRANSITIONS.put(Status.ACTIVE, EnumSet.of(Status.COMPLETED, Status.CANCELLED));
        VALID_TRANSITIONS.put(Status.COMPLETED, EnumSet.noneOf(Status.class)); // Final
        VALID_TRANSITIONS.put(Status.CANCELLED, EnumSet.noneOf(Status.class)); // Final
    }

    protected ShiftStatus() {}

    private ShiftStatus(Status value) {
        this.value = value;
    }

    public static ShiftStatus active() {
        return new ShiftStatus(Status.ACTIVE);
    }

    public boolean canTransitionTo(Status next) {
        return VALID_TRANSITIONS.getOrDefault(value, EnumSet.noneOf(Status.class)).contains(next);
    }

    public ShiftStatus complete() {
        if (!canTransitionTo(Status.COMPLETED)) {
            throw new IllegalStateException("Cannot complete shift in current state: " + value);
        }
        return new ShiftStatus(Status.COMPLETED);
    }

    public ShiftStatus cancel() {
        if (!canTransitionTo(Status.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel shift in current state: " + value);
        }
        return new ShiftStatus(Status.CANCELLED);
    }

    public boolean isActive() { return value == Status.ACTIVE; }
    public boolean isCompleted() { return value == Status.COMPLETED; }
    public boolean isCancelled() { return value == Status.CANCELLED; }

    public Status getValue() { return value; }
}

