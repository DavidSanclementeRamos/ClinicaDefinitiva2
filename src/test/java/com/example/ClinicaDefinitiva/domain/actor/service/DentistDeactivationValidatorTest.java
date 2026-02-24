
package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DentistDeactivationValidatorTest {

    private ScheduleRepository scheduleRepository;
    private DentistDeactivationValidator validator;
    private DentistId dentistId;
    private ScheduleQueryService schedule;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        validator = new DentistDeactivationValidator(scheduleRepository);
        dentistId = DentistId.of(1L);
        schedule = mock(ScheduleQueryService.class);
    }

    @Test
    void shouldFailWhenDentistHasAppointmentsWithin24Hours() {
        when(scheduleRepository.findByDentistId(dentistId)).thenReturn(schedule);
        when(schedule.hasAppointmentsWithinHours(dentistId, 24)).thenReturn(true);

        Outcome<Void> result = validator.validate(dentistId);

        assertTrue(result.isFailure());
        assertEquals(DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                     result.getDetalles().get(0).getCode());
    }

    @Test
    void shouldPassWhenDentistHasNoAppointmentsWithin24Hours() {
        when(scheduleRepository.findByDentistId(dentistId)).thenReturn(schedule);
        when(schedule.hasAppointmentsWithinHours(dentistId, 24)).thenReturn(false);

        Outcome<Void> result = validator.validate(dentistId);

        assertTrue(result.isSuccess());
    }
}
