
package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.PageImpl;

class DentistVacationServiceTest {

    private AppointmentRepository appointmentRepository;
    private DentistRepository dentistRepository;
    private DentistVacationService service;

    private DentistId dentistId;
    private Dentist dentist;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        dentistRepository = mock(DentistRepository.class);
        service = new DentistVacationService(appointmentRepository, dentistRepository);

        dentistId = DentistId.of(1L);
        dentist = mock(Dentist.class);
        appointment = mock(Appointment.class);
    }

    @Test
    void shouldApplyVacationWhenValidRangeAndNoConflicts() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 18, 0);

        when(dentistRepository.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(appointmentRepository.findByDentistBetween(eq(dentistId), eq(start), eq(end), any()))
                .thenReturn(new PageImpl<>(List.of()));
        assertTrue(TimeIntervalRules.isValid(start, end));

        service.validateVacationRequest(dentistId, start, end);

        verify(dentist).applyVacation(start, end);
        verify(dentistRepository).save(dentist);
    }

    @Test
    void shouldThrowExceptionWhenVacationRangeIsInvalid() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 10, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 1, 18, 0); // rango inválido

        assertThrows(BusinessRuleViolationException.class,
            () -> service.validateVacationRequest(dentistId, start, end));
    }

    @Test
    void shouldThrowExceptionWhenConflictingAppointmentsExist() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 18, 0);

        when(appointmentRepository.findByDentistBetween(eq(dentistId), eq(start), eq(end), any()))
                .thenReturn(new PageImpl<>(List.of(appointment)));

        assertThrows(BusinessRuleViolationException.class,
            () -> service.validateVacationRequest(dentistId, start, end));
    }

    @Test
    void shouldThrowExceptionWhenDentistNotFound() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 18, 0);

        when(appointmentRepository.findByDentistBetween(eq(dentistId), eq(start), eq(end), any()))
                .thenReturn(new PageImpl<>(List.of()));
        when(dentistRepository.findById(dentistId)).thenReturn(Optional.empty());

        assertThrows(DentistNotFoundException.class,
            () -> service.validateVacationRequest(dentistId, start, end));
    }
}
