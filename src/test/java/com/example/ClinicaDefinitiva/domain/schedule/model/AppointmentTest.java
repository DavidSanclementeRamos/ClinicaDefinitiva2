
package com.example.ClinicaDefinitiva.domain.schedule.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class AppointmentTest {

    private Appointment buildScheduledAppointment(LocalDateTime start, LocalDateTime end) {
        return new Appointment.Builder()
                .withDentistId(DentistId.of(10L))
                .withPatientId(PatientId.of(20L))
                .withServiceId(ServiceId.of(30L))
                .withStart(start)
                .withEnd(end)
                .withStatus(AppointmentStatus.scheduled())
                .withAppointmentType(AppointmentType.CONSULTATION)
                .build();
    }

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {
        @Test
        @DisplayName("crear cita programada")
        void create_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(2);
            LocalDateTime end = start.plusHours(1);

            Appointment appt = buildScheduledAppointment(start, end);

            assertThat(appt.esFutura()).isTrue();
            assertThat(appt.getStart()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Cancelación")
    class CancellationTests {

        @Test
        @DisplayName("cancelar con motivo válido y fuera de ventana de 24h")
        void cancel_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(2);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            appt.cancel("Paciente indispuesto");

            assertThat(appt.getStatus().isCancelled()).isTrue();
        }

        @Test
        @DisplayName("cancelar sin motivo -> excepción")
        void cancel_missingReason_throws() {
            LocalDateTime start = LocalDateTime.now().plusDays(2);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            assertThatThrownBy(() -> appt.cancel(" "))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_MISSING_REASON));
        }

        @Test
        @DisplayName("cancelar dentro de 24h -> excepción")
        void cancel_late_throws() {
            LocalDateTime start = LocalDateTime.now().plusHours(12);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            assertThatThrownBy(() -> appt.cancel("Paciente indispuesto"))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_LATE_CANCELLATION));
        }
    }

    @Nested
    @DisplayName("Finalización")
    class CompletionTests {

        @Test
        @DisplayName("finalizar con completion válido")
        void complete_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            AppointmentCompletion completion = new AppointmentCompletion(ServiceDuration.of(30), "Notas clínicas");
            appt.complete(completion, DentistId.of(10L));

            assertThat(appt.getStatus().isCompleted()).isTrue();
            assertThat(appt.getCompletion()).isEqualTo(completion);
        }

        @Test
        @DisplayName("finalizar sin completion -> excepción")
        void complete_missingCompletion_throws() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            assertThatThrownBy(() -> appt.complete(null, DentistId.of(10L)))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION));
        }
    }

    @Nested
    @DisplayName("No-show")
    class NoShowTests {

        @Test
        @DisplayName("marcar como no-show con motivo válido")
        void noShow_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            appt.markAsNoShow("Paciente no asistió");

            assertThat(appt.getStatus().isNoShow()).isTrue();
        }

        @Test
        @DisplayName("marcar como no-show sin motivo -> excepción")
        void noShow_missingReason_throws() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            assertThatThrownBy(() -> appt.markAsNoShow(" "))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                            .isEqualTo(AppointmentError.ERR_APPT_MISSING_REASON));
        }
    }

    @Nested
    @DisplayName("Métodos auxiliares")
    class HelperMethodsTests {

        @Test
        @DisplayName("esFutura devuelve true si la cita es posterior a ahora")
        void esFutura_valid() {
            Appointment appt = buildScheduledAppointment(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
            assertThat(appt.esFutura()).isTrue();
        }

        @Test
        @DisplayName("isWithinNext24Hours devuelve true si la cita está dentro de 24h")
        void withinNext24Hours_valid() {
            LocalDateTime ref = LocalDateTime.now();
            Appointment appt = buildScheduledAppointment(ref.plusHours(12), ref.plusHours(13));
            assertThat(appt.isWithinNext24Hours(ref)).isTrue();
        }

        @Test
        @DisplayName("conflictsWith devuelve true si se solapan horarios")
        void conflicts_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            assertThat(appt.conflictsWith(start.plusMinutes(30), start.plusHours(2))).isTrue();
        }

        @Test
        @DisplayName("conflictsWith devuelve false si no se solapan horarios")
        void conflicts_invalid() {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            Appointment appt = buildScheduledAppointment(start, start.plusHours(1));

            assertThat(appt.conflictsWith(start.plusHours(2), start.plusHours(3))).isFalse();
        }
    }
}
