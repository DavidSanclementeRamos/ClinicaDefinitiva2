package com.example.ClinicaDefinitiva.domain.schudele;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentStatusTest {

    @Test
    void scheduledCanTransitionToConfirmed() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertTrue(status.canTransitionTo(AppointmentStatus.Status.CONFIRMED));
    }

    @Test
    void scheduledCannotTransitionToCompleted() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertFalse(status.canTransitionTo(AppointmentStatus.Status.COMPLETED));
        assertThrows(ValueObjectValidationException.class,
                () -> status.transitionTo(AppointmentStatus.Status.COMPLETED));
    }

    @Test
    void confirmedCanTransitionToCompleted() {
        AppointmentStatus status = AppointmentStatus.from(AppointmentStatus.Status.CONFIRMED);
        AppointmentStatus next = status.transitionTo(AppointmentStatus.Status.COMPLETED);
        assertTrue(next.isCompleted());
    }

    @Test
    void finalStateCannotTransition() {
        AppointmentStatus status = AppointmentStatus.from(AppointmentStatus.Status.COMPLETED);
        assertTrue(status.isFinalState());
        assertThrows(ValueObjectValidationException.class,
                () -> status.transitionTo(AppointmentStatus.Status.CANCELLED));
    }
}

