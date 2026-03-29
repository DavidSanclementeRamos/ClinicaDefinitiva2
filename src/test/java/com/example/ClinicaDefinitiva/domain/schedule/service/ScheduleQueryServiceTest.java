package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleQueryServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private ScheduleQueryService queryService;

    private static final DentistId DENTIST_ID = DentistId.of(1L);
    private static final PatientId PATIENT_ID = PatientId.of(1L);

    @Test
    @DisplayName("hasAppointmentsWithinHours retorna true si hay citas")
    void hasAppointmentsWithinHours_returnsTrue() {
        when(appointmentRepository.existsScheduledByDentistBetween(any(), any(), any()))
                .thenReturn(true);

        boolean result = queryService.hasAppointmentsWithinHours(DENTIST_ID, 24);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("hasAppointmentsWithinDays retorna false si no hay citas")
    void hasAppointmentsWithinDays_returnsFalse() {
        when(appointmentRepository.existsScheduledByPatientBetween(any(), any(), any()))
                .thenReturn(false);

        boolean result = queryService.hasAppointmentsWithin(PATIENT_ID, 7);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("findAppointmentsWithinHours retorna lista de citas")
    void findAppointmentsWithinHours_returnsList() {
        Appointment appointment = mock(Appointment.class);
        when(appointment.getStatus()).thenReturn(AppointmentStatus.scheduled());
        when(appointmentRepository.findByDentistBetween(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(appointment)));

        List<Appointment> result = queryService.findAppointmentsWithinHours(DENTIST_ID, 24);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("getTotalOccupiedTime calcula tiempo ocupado")
    void getTotalOccupiedTime_calculatesCorrectly() {
        Appointment appointment1 = mock(Appointment.class);
        Appointment appointment2 = mock(Appointment.class);
        when(appointment1.getStart()).thenReturn(LocalDateTime.now().withHour(9).withMinute(0));
        when(appointment1.getEnd()).thenReturn(LocalDateTime.now().withHour(10).withMinute(0));
        when(appointment2.getStart()).thenReturn(LocalDateTime.now().withHour(11).withMinute(0));
        when(appointment2.getEnd()).thenReturn(LocalDateTime.now().withHour(12).withMinute(0));
        when(appointment1.getStatus()).thenReturn(AppointmentStatus.scheduled());
        when(appointment2.getStatus()).thenReturn(AppointmentStatus.scheduled());

        when(appointmentRepository.findByDentistAndDate(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(appointment1, appointment2)));

        var result = queryService.getTotalOccupiedTime(DENTIST_ID, LocalDate.now());

        assertThat(result.toMinutes()).isEqualTo(120);
    }

    @Test
    @DisplayName("getActiveAppointmentCount retorna conteo")
    void getActiveAppointmentCount_returnsCount() {
        when(appointmentRepository.countScheduledByDentist(DENTIST_ID)).thenReturn(5L);

        long count = queryService.getActiveAppointmentCount(DENTIST_ID);

        assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("hasConflictingAppointments retorna true si hay conflicto")
    void hasConflictingAppointments_returnsTrue() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        when(appointmentRepository.existsScheduledByDentistBetween(DENTIST_ID, start, end))
                .thenReturn(true);

        boolean result = queryService.hasConflictingAppointments(DENTIST_ID, start, end);

        assertThat(result).isTrue();
    }
}
