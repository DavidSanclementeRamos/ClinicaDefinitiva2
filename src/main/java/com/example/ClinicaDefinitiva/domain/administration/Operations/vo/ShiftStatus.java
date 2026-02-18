package com.example.ClinicaDefinitiva.domain.administration.Operations.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

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

            throw new ValueObjectValidationException(OperationsVoError.ERR_SHIFT_INVALID_COMPLETION, VOContext.OPERATIONS);
        }
        return new ShiftStatus(Status.COMPLETED);
    }

    public ShiftStatus cancel() {
        if (!canTransitionTo(Status.CANCELLED)) {
            throw new ValueObjectValidationException(OperationsVoError.ERR_SHIFT_INVALID_CANCELLATION, VOContext.OPERATIONS);
        }
        return new ShiftStatus(Status.CANCELLED);
    }

    public boolean isActive() { return value == Status.ACTIVE; }
    public boolean isCompleted() { return value == Status.COMPLETED; }
    public boolean isCancelled() { return value == Status.CANCELLED; }

    public Status getValue() { return value; }
}

