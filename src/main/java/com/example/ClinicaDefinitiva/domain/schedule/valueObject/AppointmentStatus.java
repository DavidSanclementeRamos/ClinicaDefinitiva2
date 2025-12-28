package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public final class AppointmentStatus {
    public enum Status {
        SCHEDULED,
        CONFIRMED,
        COMPLETED,
        CANCELLED,
        NO_SHOW,
        RESCHEDULED
    }

    private Status value;

    // Máquina de estados: transiciones válidas
    private static final EnumMap<Status, Set<Status>> VALID_TRANSITIONS = new EnumMap<>(Status.class);

    static {
        VALID_TRANSITIONS.put(Status.SCHEDULED, EnumSet.of(
                Status.CONFIRMED,
                Status.CANCELLED,
                Status.RESCHEDULED,
                Status.NO_SHOW
        ));
        VALID_TRANSITIONS.put(Status.CONFIRMED, EnumSet.of(
                Status.COMPLETED,
                Status.CANCELLED,
                Status.RESCHEDULED,
                Status.NO_SHOW
        ));
        VALID_TRANSITIONS.put(Status.RESCHEDULED, EnumSet.of(
                Status.SCHEDULED,
                Status.CANCELLED
        ));
        VALID_TRANSITIONS.put(Status.COMPLETED, EnumSet.noneOf(Status.class)); // Estado final
        VALID_TRANSITIONS.put(Status.CANCELLED, EnumSet.noneOf(Status.class)); // Estado final
        VALID_TRANSITIONS.put(Status.NO_SHOW, EnumSet.noneOf(Status.class)); // Estado final
    }

    private AppointmentStatus(Status value) {
        if (value == null) {
            throw new ValueObjectValidationException(ScheduleVOError.ERR_APPOINTMENT_STATUS_REQUIRED,VOContext.AVAILABILITY_STATUS);
        }
        this.value = value;
    }

    public static AppointmentStatus from(Status value) {
        return new AppointmentStatus(value);
    }

    public static AppointmentStatus scheduled() {
        return new AppointmentStatus(Status.SCHEDULED);
    }

    // Patrón de transición segura
    public boolean canTransitionTo(Status next) {
        return VALID_TRANSITIONS.getOrDefault(value, EnumSet.noneOf(Status.class)).contains(next);
    }

    public AppointmentStatus transitionTo(Status next) {
        if (!canTransitionTo(next)) {
            throw new ValueObjectValidationException(ScheduleVOError.ERR_APPOINTMENT_STATUS_INVALID_TRANSITION, VOContext.AVAILABILITY_STATUS);
        }
        return new AppointmentStatus(next);
    }

    // Queries semánticas (getters)
    public boolean isScheduled() { return value == Status.SCHEDULED; }
    public boolean isConfirmed() { return value == Status.CONFIRMED; }
    public boolean isCancelled() { return value == Status.CANCELLED; }
    public boolean isCompleted() { return value == Status.COMPLETED; }
    public boolean isNoShow() { return value == Status.NO_SHOW; }
    public boolean isRescheduled() { return value == Status.RESCHEDULED; }

    // Queries de negocio
    public boolean isEditable() {
        return value == Status.SCHEDULED || value == Status.CONFIRMED;
    }

    public boolean isFinalState() {
        return value == Status.COMPLETED ||
                value == Status.CANCELLED ||
                value == Status.NO_SHOW;
    }

    public Status getValue() { return value; }

    @Override
    public String toString() { return value.name(); }
}