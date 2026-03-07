package com.example.ClinicaDefinitiva.domain.schudele.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentStatusTest {

    @Test
    void scheduledCanTransitionToCancelled() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertTrue(status.canTransitionTo(AppointmentStatus.Status.CANCELLED));
    }

    @Test
    void scheduledCanTransitionToCompleted() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertTrue(status.canTransitionTo(AppointmentStatus.Status.COMPLETED));
        AppointmentStatus next = status.transitionTo(AppointmentStatus.Status.COMPLETED);
        assertTrue(next.isCompleted());
    }

    @Test
    void scheduledCanTransitionToNoShow() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertTrue(status.canTransitionTo(AppointmentStatus.Status.NO_SHOW));
        AppointmentStatus next = status.transitionTo(AppointmentStatus.Status.NO_SHOW);
        assertTrue(next.isNoShow());
    }

    @Test
    void finalStateCannotTransition() {
        AppointmentStatus status = AppointmentStatus.from(AppointmentStatus.Status.COMPLETED);
        assertTrue(status.isFinalState());
        assertThrows(ValueObjectValidationException.class,
                () -> status.transitionTo(AppointmentStatus.Status.CANCELLED));
    }

    @Test
    void descriptionIsAvailable() {
        assertEquals("Cita programada", AppointmentStatus.Status.SCHEDULED.getDescription());
        assertEquals("Cita completada", AppointmentStatus.Status.COMPLETED.getDescription());
        assertEquals("Cita cancelada", AppointmentStatus.Status.CANCELLED.getDescription());
        assertEquals("Paciente no asistió", AppointmentStatus.Status.NO_SHOW.getDescription());
    }

    @Test
    void toStringIncludesDescription() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertTrue(status.toString().contains("SCHEDULED"));
        assertTrue(status.toString().contains("Cita programada"));
    }
}