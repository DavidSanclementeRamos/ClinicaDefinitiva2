
package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientDeactivationValidatorTest {

    private ScheduleRepository scheduleRepository;
    private PatientDeactivationValidator validator;
    private PatientId patientId;
    private ScheduleQueryService schedule;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        validator = new PatientDeactivationValidator(scheduleRepository, 7); // bloquea 7 días
        patientId = PatientId.of(1L);
        schedule = mock(ScheduleQueryService.class);
    }

    @Test
    void shouldFailWhenPatientHasAppointmentsWithinBlockDays() {
        when(scheduleRepository.findByPatientId(patientId)).thenReturn(schedule);
        when(schedule.hasAppointmentsWithin(patientId, 7)).thenReturn(true);

        Outcome<Void> result = validator.validate(patientId);

        assertTrue(result.isFailure());
        assertEquals(PatientError.ERR_PATIENT_ACTIVE_SERVICES,
                     result.getDetalles().get(0).getCode());
    }

    @Test
    void shouldPassWhenPatientHasNoAppointmentsWithinBlockDays() {
        when(scheduleRepository.findByPatientId(patientId)).thenReturn(schedule);
        when(schedule.hasAppointmentsWithin(patientId, 7)).thenReturn(false);

        Outcome<Void> result = validator.validate(patientId);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldPassWhenScheduleIsNull() {
        when(scheduleRepository.findByPatientId(patientId)).thenReturn(null);

        Outcome<Void> result = validator.validate(patientId);

        assertTrue(result.isSuccess());
    }
}
