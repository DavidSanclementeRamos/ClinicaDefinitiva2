package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.service.DentistDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.service.PatientDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDeactivationPolicyTest {

    @Mock
    private DentistRepository dentistRepo;
    @Mock
    private PatientRepository patientRepo;
    @Mock
    private DentistDeactivationValidator dentistValidator;
    @Mock
    private PatientDeactivationValidator patientValidator;

    @InjectMocks
    private UserDeactivationPolicy policy;

    private UserIdentity user;
    private Dentist dentist;

    @BeforeEach
    void setUp() {
        user = mock(UserIdentity.class);
        when(user.getId()).thenReturn(com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId.from(1L));

        dentist = mock(Dentist.class);
        when(dentist.getDentistId()).thenReturn(com.example.ClinicaDefinitiva.domain.actor.vo.DentistId.of(1L));
    }

    @Test
    void validate_shouldFailWhenDentistHasActiveAppointments() {
        when(dentistRepo.findByUserId(user.getId())).thenReturn(Optional.of(dentist));
        when(patientRepo.findByUserId(user.getId())).thenReturn(Optional.empty());

        OutcomeDetail detail = new OutcomeDetail(
                DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                ErrorSeverity.INFO,
                Category.CLINICO,
                EntityContext.DENTIST
        );
        Outcome<Void> failOutcome = Outcome.fail(detail);
        when(dentistValidator.validate(dentist.getDentistId())).thenReturn(failOutcome);

        Outcome<Void> result = policy.validate(user);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getDetalles()).contains(detail);
        verify(dentistValidator).validate(dentist.getDentistId());
        verify(patientValidator, never()).validate(any());
    }

    @Test
    void validate_shouldSucceedWhenNoActiveAppointments() {
        when(dentistRepo.findByUserId(user.getId())).thenReturn(Optional.of(dentist));
        when(patientRepo.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(dentistValidator.validate(dentist.getDentistId())).thenReturn(Outcome.ok());

        Outcome<Void> result = policy.validate(user);

        assertThat(result.isSuccess()).isTrue();
        verify(dentistValidator).validate(dentist.getDentistId());
        verify(patientValidator, never()).validate(any());
    }
}