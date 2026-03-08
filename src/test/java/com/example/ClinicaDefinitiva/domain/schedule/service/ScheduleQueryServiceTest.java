
package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class ScheduleQueryServiceTest {

    private AppointmentRepository appointmentRepository;
    private ScheduleQueryService service;

    private DentistId dentistId = DentistId.of(1L);
    private PatientId patientId = PatientId.of(2L);

    @BeforeEach
    void setup() {
        appointmentRepository = mock(AppointmentRepository.class);
        service = new ScheduleQueryService(appointmentRepository);
    }

    private Appointment scheduledAppointment(LocalDateTime start, LocalDateTime end) {
        Appointment appt = mock(Appointment.class);
        when(appt.getStart()).thenReturn(start);
        when(appt.getEnd()).thenReturn(end);
        when(appt.getStatus()).thenReturn(AppointmentStatus.scheduled());
        return appt;
    }

    @Nested
    @DisplayName("findAppointmentsWithinHours")
    class FindWithinHoursTests {

        @Test
        @DisplayName("devuelve citas programadas dentro de N horas")
        void findWithinHours_valid() {
            LocalDateTime now = LocalDateTime.now();
            Appointment appt = scheduledAppointment(now.plusHours(2), now.plusHours(3));

            when(appointmentRepository.findByDentistBetween(eq(dentistId), any(), any(), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt)));

            List<Appointment> result = service.findAppointmentsWithinHours(dentistId, 5);

            assertThat(result).containsExactly(appt);
        }

        @Test
        @DisplayName("hasAppointmentsWithinHours devuelve true si hay citas")
        void hasWithinHours_true() {
            LocalDateTime now = LocalDateTime.now();
            Appointment appt = scheduledAppointment(now.plusHours(2), now.plusHours(3));

            when(appointmentRepository.findByDentistBetween(eq(dentistId), any(), any(), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt)));

            assertThat(service.hasAppointmentsWithinHours(dentistId, 5)).isTrue();
        }
    }

    @Nested
    @DisplayName("findAppointmentsOn")
    class FindOnTests {

        @Test
        @DisplayName("devuelve citas programadas en una fecha")
        void findOn_valid() {
            LocalDate date = LocalDate.now().plusDays(1);
            Appointment appt = scheduledAppointment(date.atTime(10, 0), date.atTime(11, 0));

            when(appointmentRepository.findByDentistAndDate(eq(dentistId), eq(date), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt)));

            List<Appointment> result = service.findAppointmentsOn(dentistId, date);

            assertThat(result).containsExactly(appt);
        }
    }

    @Nested
    @DisplayName("findAppointmentsWithin (days)")
    class FindWithinDaysTests {

        @Test
        @DisplayName("devuelve citas programadas dentro de N días para dentista")
        void findWithinDays_dentist() {
            LocalDateTime now = LocalDateTime.now();
            Appointment appt = scheduledAppointment(now.plusDays(2), now.plusDays(2).plusHours(1));

            when(appointmentRepository.findByDentistBetween(eq(dentistId), any(), any(), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt)));

            List<Appointment> result = service.findAppointmentsWithin(dentistId, 3);

            assertThat(result).containsExactly(appt);
        }

        @Test
        @DisplayName("devuelve citas programadas dentro de N días para paciente")
        void findWithinDays_patient() {
            LocalDateTime now = LocalDateTime.now();
            Appointment appt = scheduledAppointment(now.plusDays(2), now.plusDays(2).plusHours(1));

            when(appointmentRepository.findByPatientBetween(eq(patientId), any(), any(), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt)));

            List<Appointment> result = service.findAppointmentsWithin(patientId, 3);

            assertThat(result).containsExactly(appt);
        }
    }

    @Nested
    @DisplayName("getTotalOccupiedTime")
    class TotalTimeTests {

        @Test
        @DisplayName("calcula duración total de citas en una fecha")
        void totalTime_valid() {
            LocalDate date = LocalDate.now().plusDays(1);
            Appointment appt1 = scheduledAppointment(date.atTime(9, 0), date.atTime(10, 0));
            Appointment appt2 = scheduledAppointment(date.atTime(11, 0), date.atTime(12, 30));

            when(appointmentRepository.findByDentistAndDate(eq(dentistId), eq(date), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt1, appt2)));

            Duration result = service.getTotalOccupiedTime(dentistId, date);

            assertThat(result).isEqualTo(Duration.ofMinutes(150));
        }
    }

    @Nested
    @DisplayName("getActiveAppointmentCount")
    class ActiveCountTests {

        @Test
        @DisplayName("cuenta citas activas programadas")
        void activeCount_valid() {
            Appointment appt1 = scheduledAppointment(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
            Appointment appt2 = scheduledAppointment(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1));

            when(appointmentRepository.findByDentist(eq(dentistId), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt1, appt2)));

            int count = service.getActiveAppointmentCount(dentistId);

            assertThat(count).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("hasConflictingAppointments")
    class ConflictsTests {

        @Test
        @DisplayName("devuelve true si hay conflicto con citas existentes")
        void conflicts_true() {
            LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
            LocalDateTime end = start.plusHours(1);

            Appointment appt = scheduledAppointment(start.plusMinutes(30), end.plusMinutes(30));
            when(appt.conflictsWith(start, end)).thenReturn(true);

            when(appointmentRepository.findByDentistBetween(eq(dentistId), eq(start), eq(end), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(appt)));

            assertThat(service.hasConflictingAppointments(dentistId, start, end)).isTrue();
        }
    }
}
