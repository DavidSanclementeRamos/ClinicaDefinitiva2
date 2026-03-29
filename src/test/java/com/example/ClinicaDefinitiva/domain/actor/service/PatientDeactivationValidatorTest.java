package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.PatientError;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientDeactivationValidatorTest {

    @Mock
    private ScheduleQueryService scheduleQueryService;

    private PatientDeactivationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PatientDeactivationValidator(scheduleQueryService, 7);
    }

    @Test
    @DisplayName("Validar paciente sin citas próximas (éxito)")
    void validateSuccess() {
        PatientId patientId = PatientId.of(1L);
        when(scheduleQueryService.hasAppointmentsWithin(patientId, 7)).thenReturn(false);

        Outcome<Void> outcome = validator.validate(patientId);

        assertThat(outcome.isSuccess()).isTrue();
        verify(scheduleQueryService).hasAppointmentsWithin(patientId, 7);
    }

    @Test
    @DisplayName("Validar paciente con citas próximas (fallo)")
    void validateFail() {
        PatientId patientId = PatientId.of(1L);
        when(scheduleQueryService.hasAppointmentsWithin(patientId, 7)).thenReturn(true);

        Outcome<Void> outcome = validator.validate(patientId);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(PatientError.ERR_PATIENT_ACTIVE_SERVICES);
        verify(scheduleQueryService).hasAppointmentsWithin(patientId, 7);
    }
}

