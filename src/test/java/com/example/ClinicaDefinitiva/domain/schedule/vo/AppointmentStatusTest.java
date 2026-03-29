package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AppointmentStatusTest {

    @Test
    @DisplayName("Crear estado SCHEDULED")
    void scheduled() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        assertThat(status.isScheduled()).isTrue();
        assertThat(status.isEditable()).isTrue();
        assertThat(status.isFinalState()).isFalse();
    }

    @Test
    @DisplayName("Transición válida: SCHEDULED → COMPLETED")
    void transition_scheduledToCompleted() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        AppointmentStatus newStatus = status.transitionTo(AppointmentStatus.Status.COMPLETED);
        assertThat(newStatus.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Transición válida: SCHEDULED → CANCELLED")
    void transition_scheduledToCancelled() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        AppointmentStatus newStatus = status.transitionTo(AppointmentStatus.Status.CANCELLED);
        assertThat(newStatus.isCancelled()).isTrue();
    }

    @Test
    @DisplayName("Transición válida: SCHEDULED → NO_SHOW")
    void transition_scheduledToNoShow() {
        AppointmentStatus status = AppointmentStatus.scheduled();
        AppointmentStatus newStatus = status.transitionTo(AppointmentStatus.Status.NO_SHOW);
        assertThat(newStatus.isNoShow()).isTrue();
    }

    @Test
    @DisplayName("Transición inválida: COMPLETED → SCHEDULED")
    void transition_completedToScheduled_throws() {
        AppointmentStatus status = AppointmentStatus.from(AppointmentStatus.Status.COMPLETED);
        assertThatThrownBy(() -> status.transitionTo(AppointmentStatus.Status.SCHEDULED))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Transición inválida: CANCELLED → COMPLETED")
    void transition_cancelledToCompleted_throws() {
        AppointmentStatus status = AppointmentStatus.from(AppointmentStatus.Status.CANCELLED);
        assertThatThrownBy(() -> status.transitionTo(AppointmentStatus.Status.COMPLETED))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Estados finales")
    void finalStates() {
        assertThat(AppointmentStatus.from(AppointmentStatus.Status.COMPLETED).isFinalState()).isTrue();
        assertThat(AppointmentStatus.from(AppointmentStatus.Status.CANCELLED).isFinalState()).isTrue();
        assertThat(AppointmentStatus.from(AppointmentStatus.Status.NO_SHOW).isFinalState()).isTrue();
        assertThat(AppointmentStatus.scheduled().isFinalState()).isFalse();
    }
}