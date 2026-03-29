package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistDeactivationValidatorTest {

    @Mock
    private ScheduleQueryService scheduleQueryService;

    @InjectMocks
    private DentistDeactivationValidator validator;

    @Test
    @DisplayName("Validar dentista sin citas próximas (éxito)")
    void validateSuccess() {
        DentistId dentistId = DentistId.of(1L);
        when(scheduleQueryService.hasAppointmentsWithinHours(dentistId, 24)).thenReturn(false);

        Outcome<Void> outcome = validator.validate(dentistId);

        assertThat(outcome.isSuccess()).isTrue();
        verify(scheduleQueryService).hasAppointmentsWithinHours(dentistId, 24);
    }

    @Test
    @DisplayName("Validar dentista con citas próximas (fallo)")
    void validateFail() {
        DentistId dentistId = DentistId.of(1L);
        when(scheduleQueryService.hasAppointmentsWithinHours(dentistId, 24)).thenReturn(true);

        Outcome<Void> outcome = validator.validate(dentistId);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS);
        verify(scheduleQueryService).hasAppointmentsWithinHours(dentistId, 24);
    }
}
