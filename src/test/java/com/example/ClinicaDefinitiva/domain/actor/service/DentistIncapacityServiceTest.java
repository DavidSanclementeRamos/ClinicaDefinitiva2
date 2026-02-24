
package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.PageImpl;

class DentistIncapacityServiceTest {

    private AppointmentRepository appointmentRepository;
    private DentistRepository dentistRepository;
    private DentistIncapacityService service;

    private DentistId dentistId;
    private Dentist dentist;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        dentistRepository = mock(DentistRepository.class);
        service = new DentistIncapacityService(appointmentRepository, dentistRepository);

        dentistId = DentistId.of(1L);
        dentist = mock(Dentist.class);
        appointment = mock(Appointment.class);
    }

    @Test
    void shouldRegisterIncapacityAndCancelAppointments() {
        LocalDateTime start = LocalDateTime.of(2026, 2, 20, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 25, 18, 0);
        String note = "Medical leave";

        when(dentistRepository.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(appointmentRepository.findByDentistBetween(eq(dentistId), eq(start), eq(end), any()))
                .thenReturn(new PageImpl<>(List.of(appointment)));

        service.registerIncapacity(dentistId, start, end, note);

        verify(dentist).applyIncapacity(start, end, note);
        verify(dentistRepository).save(dentist);
        verify(appointment).cancel(note);
    }

    @Test
    void shouldThrowExceptionWhenDentistNotFound() {
        when(dentistRepository.findById(dentistId)).thenReturn(Optional.empty());

        assertThrows(DentistNotFoundException.class,
            () -> service.registerIncapacity(dentistId,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    "Medical leave"));
    }

    @Test
    void shouldHandleNoAppointmentsToCancel() {
        LocalDateTime start = LocalDateTime.of(2026, 2, 20, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 25, 18, 0);
        String note = "Medical leave";

        when(dentistRepository.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(appointmentRepository.findByDentistBetween(eq(dentistId), eq(start), eq(end), any()))
                .thenReturn(new PageImpl<>(List.of()));

        service.registerIncapacity(dentistId, start, end, note);

        verify(dentist).applyIncapacity(start, end, note);
        verify(dentistRepository).save(dentist);
        verify(appointment, never()).cancel(any());
    }
}