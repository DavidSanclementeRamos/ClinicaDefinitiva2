package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class AppointmentTest {

    private Appointment appointment;
    private final LocalDateTime start = LocalDateTime.now().plusDays(2);
    private final LocalDateTime end = start.plusHours(1);

    @BeforeEach
    void setUp() {
        appointment = new Appointment.Builder()
                .withId(AppointmentId.of(1L))
                .withDentistId(DentistId.of(1L))
                .withPatientId(PatientId.of(1L))
                .withServiceId(ServiceId.of(1L))
                .withStart(start)
                .withEnd(end)
                .withReason("Dolor dental")
                .withAppointmentType(AppointmentType.EMERGENCY_VISIT)
                .build();
    }

    @Test
    @DisplayName("SCH-UNIT-001: Crear cita con fecha futura en estado SCHEDULED")
    void create_shouldBeScheduled() {
        assertThat(appointment.getStatus().isScheduled()).isTrue();
        assertThat(appointment.getStart()).isEqualTo(start);
        assertThat(appointment.getEnd()).isEqualTo(end);
    }

    @Test
    @DisplayName("SCH-UNIT-002: Cancelar cita dentro de 24h lanza excepción")
    void cancel_within24Hours_shouldThrow() {
        Appointment soonAppointment = new Appointment.Builder()
                .withId(AppointmentId.of(2L))
                .withDentistId(DentistId.of(1L))
                .withPatientId(PatientId.of(1L))
                .withServiceId(ServiceId.of(1L))
                .withStart(LocalDateTime.now().plusHours(12))
                .withEnd(LocalDateTime.now().plusHours(13))
                .withReason("Dolor")
                .withAppointmentType(AppointmentType.EMERGENCY_VISIT)
                .build();

        assertThatThrownBy(() -> soonAppointment.cancel("Motivo válido"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("SCH-UNIT-003: Cancelar cita fuera de 24h exitosamente")
    void cancel_outside24Hours_shouldSucceed() {
        appointment.cancel("Motivo válido con más de 10 caracteres");
        assertThat(appointment.getStatus().isCancelled()).isTrue();
    }

    @Test
    @DisplayName("SCH-UNIT-004: Cancelar sin motivo lanza excepción")
    void cancel_withoutReason_shouldThrow() {
        assertThatThrownBy(() -> appointment.cancel(null))
                .isInstanceOf(BusinessRuleViolationException.class);
        assertThatThrownBy(() -> appointment.cancel(""))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("SCH-UNIT-005: Completar cita con notas clínicas")
    void complete_shouldSucceed() {
        ServiceDuration actualDuration = ServiceDuration.of(45);
        AppointmentCompletion completion = new AppointmentCompletion(actualDuration, "Notas clínicas detalladas");

        appointment.complete(completion);

        assertThat(appointment.getStatus().isCompleted()).isTrue();
        assertThat(appointment.getCompletion()).isEqualTo(completion);
    }

    @Test
    @DisplayName("SCH-UNIT-006: Completar cita cancelada lanza excepción")
    void complete_cancelledAppointment_shouldThrow() {
        appointment.cancel("Motivo válido");
        ServiceDuration actualDuration = ServiceDuration.of(45);
        AppointmentCompletion completion = new AppointmentCompletion(actualDuration, "Notas");

        assertThatThrownBy(() -> appointment.complete(completion))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("SCH-UNIT-007: Marcar como no-show con motivo")
    void markAsNoShow_shouldSucceed() {
        appointment.markAsNoShow("Paciente no asistió sin aviso");
        assertThat(appointment.getStatus().isNoShow()).isTrue();
    }

    @Test
    @DisplayName("SCH-UNIT-008: Marcar como no-show sin motivo lanza excepción")
    void markAsNoShow_withoutReason_shouldThrow() {
        assertThatThrownBy(() -> appointment.markAsNoShow(null))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("conflictsWith: citas solapadas")
    void conflictsWith_overlapping_shouldReturnTrue() {
        LocalDateTime candidateStart = start.plusMinutes(30);
        LocalDateTime candidateEnd = end.plusMinutes(30);
        assertThat(appointment.conflictsWith(candidateStart, candidateEnd)).isTrue();
    }

    @Test
    @DisplayName("conflictsWith: citas no solapadas")
    void conflictsWith_nonOverlapping_shouldReturnFalse() {
        LocalDateTime candidateStart = end.plusMinutes(1);
        LocalDateTime candidateEnd = candidateStart.plusHours(1);
        assertThat(appointment.conflictsWith(candidateStart, candidateEnd)).isFalse();
    }

    @Test
    @DisplayName("isWithinNext24Hours: cita en las próximas 24h")
    void isWithinNext24Hours_shouldReturnTrue() {
        LocalDateTime soonStart = LocalDateTime.now().plusHours(12);
        Appointment soonAppointment = new Appointment.Builder()
                .withId(AppointmentId.of(3L))
                .withDentistId(DentistId.of(1L))
                .withPatientId(PatientId.of(1L))
                .withServiceId(ServiceId.of(1L))
                .withStart(soonStart)
                .withEnd(soonStart.plusHours(1))
                .withReason("Dolor")
                .withAppointmentType(AppointmentType.EMERGENCY_VISIT)
                .build();

        assertThat(soonAppointment.isWithinNext24Hours(LocalDateTime.now())).isTrue();
    }

    @Test
    @DisplayName("esFutura: cita futura")
    void esFutura_future_shouldReturnTrue() {
        assertThat(appointment.esFutura()).isTrue();
    }

    @Test
    @DisplayName("esFutura: cita pasada")
    void esFutura_past_shouldReturnFalse() {
        Appointment pastAppointment = new Appointment.Builder()
                .withId(AppointmentId.of(4L))
                .withDentistId(DentistId.of(1L))
                .withPatientId(PatientId.of(1L))
                .withServiceId(ServiceId.of(1L))
                .withStart(LocalDateTime.now().minusDays(1))
                .withEnd(LocalDateTime.now().minusDays(1).plusHours(1))
                .withReason("Dolor")
                .withAppointmentType(AppointmentType.EMERGENCY_VISIT)
                .build();

        assertThat(pastAppointment.esFutura()).isFalse();
    }
}
