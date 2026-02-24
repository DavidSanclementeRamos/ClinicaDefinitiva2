package com.example.ClinicaDefinitiva.domain.administration.Operations.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public final class ShiftStatus {

    public enum Status {
        ACTIVE("Activo"),
        COMPLETED("Completado"),
        CANCELLED("Cancelado");

        private final String description;
        Status(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    private final Status value;

    private static final EnumMap<Status, Set<Status>> VALID_TRANSITIONS = new EnumMap<>(Status.class);

    static {
        VALID_TRANSITIONS.put(Status.ACTIVE, EnumSet.of(Status.COMPLETED, Status.CANCELLED));
        VALID_TRANSITIONS.put(Status.COMPLETED, EnumSet.noneOf(Status.class)); // Final
        VALID_TRANSITIONS.put(Status.CANCELLED, EnumSet.noneOf(Status.class)); // Final
    }

    private ShiftStatus(Status value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                OperationsVoError.ERR_SHIFT_STATUS_NULL,
                VOContext.OPERATIONS
            );
        }
        this.value = value;
    }

    public static ShiftStatus of(Status status) {
        return new ShiftStatus(status);
    }

    public boolean canTransitionTo(Status next) {
        return VALID_TRANSITIONS.getOrDefault(value, EnumSet.noneOf(Status.class)).contains(next);
    }

    public ShiftStatus complete() {
        if (!canTransitionTo(Status.COMPLETED)) {
            throw new ValueObjectValidationException(
                OperationsVoError.ERR_SHIFT_INVALID_COMPLETION,
                VOContext.OPERATIONS
            );
        }
        return new ShiftStatus(Status.COMPLETED);
    }

    public ShiftStatus cancel() {
        if (!canTransitionTo(Status.CANCELLED)) {
            throw new ValueObjectValidationException(
                OperationsVoError.ERR_SHIFT_INVALID_CANCELLATION,
                VOContext.OPERATIONS
            );
        }
        return new ShiftStatus(Status.CANCELLED);
    }

    // Queries semánticas
    public boolean isActive() { return value == Status.ACTIVE; }
    public boolean isCompleted() { return value == Status.COMPLETED; }
    public boolean isCancelled() { return value == Status.CANCELLED; }

    public String getDescription() { return value.getDescription(); }
    public Status getValue() { return value; }

    @Override
    public String toString() { return value.name(); }
}