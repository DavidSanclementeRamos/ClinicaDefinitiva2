
package com.example.ClinicaDefinitiva.domain.schudele.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.AppointmentSchedulingService;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentSchedulingServiceTest {

    private AppointmentRepository appointmentRepository;
    private ShiftRepository shiftRepository;
    private ScheduleQueryService scheduleQueryService;
    private AppointmentSchedulingService service;

    private DentistId dentistId = new DentistId(1L);
    private PatientId patientId = new PatientId(2L);
    private ServiceId serviceId = new ServiceId(3L);

    @BeforeEach
    void setup() {
        appointmentRepository = mock(AppointmentRepository.class);
        shiftRepository = mock(ShiftRepository.class);
        scheduleQueryService = mock(ScheduleQueryService.class);
        service = new AppointmentSchedulingService(appointmentRepository, shiftRepository, scheduleQueryService);
    }

    @Nested
    @DisplayName("Agendar cita")
    class ScheduleTests {

        @Test
        @DisplayName("agenda cita válida sin conflictos")
        void schedule_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(2);
            LocalDateTime end = start.plusHours(1);

            Shift shift = mock(Shift.class);
            when(shift.canAccommodateAppointment(start, end)).thenReturn(true);
            when(shiftRepository.findActiveByDentistAndDate(dentistId, start.toLocalDate()))
                    .thenReturn(List.of(shift));

            Appointment expected = new Appointment.Builder()
                    .withDentistId(dentistId)
                    .withPatientId(patientId)
                    .withServiceId(serviceId)
                    .withStart(start)
                    .withEnd(end)
                    .withAppointmentType(AppointmentType.CONSULTATION)
                    .withReason("Consulta general")
                    .build();

            when(appointmentRepository.save(any(Appointment.class))).thenReturn(expected);

            Appointment result = service.scheduleAppointment(
                    dentistId, patientId, start, end,
                    AppointmentType.CONSULTATION, "Consulta general", serviceId
            );

            assertThat(result).isEqualTo(expected);
            verify(appointmentRepository).save(any(Appointment.class));
        }

        @Test
        @DisplayName("lanza excepción si hay conflicto con dentista")
        void schedule_conflictDentist_throws() {
            LocalDateTime start = LocalDateTime.now().plusDays(2);
            LocalDateTime end = start.plusHours(1);

            Shift shift = mock(Shift.class);
            when(shift.canAccommodateAppointment(start, end)).thenReturn(true);
            when(shiftRepository.findActiveByDentistAndDate(dentistId, start.toLocalDate()))
                    .thenReturn(List.of(shift));

            when(appointmentRepository.findConflictingForDentist(dentistId, start, end, true))
                    .thenReturn(List.of(mock(Appointment.class)));

            assertThatThrownBy(() -> service.scheduleAppointment(
                    dentistId, patientId, start, end,
                    AppointmentType.CONSULTATION, "Consulta general", serviceId
            ))
            .isInstanceOf(BusinessRuleViolationException.class)
            .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                    .isEqualTo(AppointmentError.ERR_APPT_DENTIST_TIME_CONFLICT));
        }
    }

    @Nested
    @DisplayName("Reagendar cita")
    class RescheduleTests {

        @Test
        @DisplayName("reagenda cita editable y fuera de 24h")
        void reschedule_valid() {
            LocalDateTime start = LocalDateTime.now().plusDays(3);
            LocalDateTime end = start.plusHours(1);

            Appointment original = new Appointment.Builder()
                    .withId(AppointmentId.of(99L))
                    .withDentistId(dentistId)
                    .withPatientId(patientId)
                    .withServiceId(serviceId)
                    .withStart(start)
                    .withEnd(end)
                    .withAppointmentType(AppointmentType.CONSULTATION)
                    .withReason("Consulta original")
                    .withStatus(AppointmentStatus.scheduled())
                    .build();

            Shift shift = mock(Shift.class);
            when(shift.canAccommodateAppointment(start.plusDays(1), end.plusDays(1))).thenReturn(true);
            when(shiftRepository.findActiveByDentistAndDate(dentistId, start.plusDays(1).toLocalDate()))
                    .thenReturn(List.of(shift));

            Appointment newAppt = new Appointment.Builder()
                    .withDentistId(dentistId)
                    .withPatientId(patientId)
                    .withServiceId(serviceId)
                    .withStart(start.plusDays(1))
                    .withEnd(end.plusDays(1))
                    .withAppointmentType(AppointmentType.CONSULTATION)
                    .withReason("Consulta original [Reprogramada]")
                    .build();

            when(appointmentRepository.save(any(Appointment.class))).thenReturn(newAppt);

            Appointment result = service.rescheduleAppointment(
                    original, dentistId, patientId,
                    start.plusDays(1), end.plusDays(1)
            );

            assertThat(result.getReason()).contains("[Reprogramada]");
            assertThat(result.getStatus().isScheduled()).isTrue();
        }

        @Test
        @DisplayName("lanza excepción si cita no es editable")
        void reschedule_notEditable_throws() {
            Appointment original = mock(Appointment.class);
            when(original.getStatus()).thenReturn(AppointmentStatus.from(AppointmentStatus.Status.COMPLETED));

            assertThatThrownBy(() -> service.rescheduleAppointment(
                    original, dentistId, patientId,
                    LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3)
            ))
            .isInstanceOf(BusinessRuleViolationException.class)
            .satisfies(ex -> assertThat(((BusinessRuleViolationException) ex).getCatalogo())
                    .isEqualTo(AppointmentError.ERR_APPT_NOT_EDITABLE));
        }
    }

    @Nested
    @DisplayName("Cancelar cita")
    class CancelTests {

        @Test
        @DisplayName("cancelar cita válida")
        void cancel_valid() {
            Appointment appt = mock(Appointment.class);
            doNothing().when(appt).cancel("Motivo válido");

            service.cancelAppointment(appt, "Motivo válido");

            verify(appt).cancel("Motivo válido");
            verify(appointmentRepository).save(appt);
        }
    }
}
