package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.service.DentistDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.service.PatientDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.service.ReceptionistDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDeactivationPolicyTest {

    private DentistRepository dentistRepo;
    private PatientRepository patientRepo;
    private GuardianRepository guardianRepo;
    private ReceptionRepository receptionistRepo;
    private DentistDeactivationValidator dentistValidator;
    private PatientDeactivationValidator patientValidator;
    private ReceptionistDeactivationValidator receptionValidator;

    private UserDeactivationPolicy policy;
    private UserIdentity user;

    @BeforeEach
    void setUp() {
        dentistRepo = mock(DentistRepository.class);
        patientRepo = mock(PatientRepository.class);
        guardianRepo = mock(GuardianRepository.class);
        receptionistRepo = mock(ReceptionRepository.class);

        dentistValidator = mock(DentistDeactivationValidator.class);
        patientValidator = mock(PatientDeactivationValidator.class);
        receptionValidator = mock(ReceptionistDeactivationValidator.class);

        policy = new UserDeactivationPolicy(
                dentistRepo, patientRepo, guardianRepo, receptionistRepo,
                dentistValidator, patientValidator, receptionValidator
        );

        user = UserIdentity.register(
                Email.of("test@example.com").getValue().get(),
                HashedPassword.fromHash("hash").getValue().get(),
                UserIdentityName.create("David").getValue().get(),
                java.time.Instant.now()
        );
    }

    @Test
    void shouldValidateDentistWhenPresent() {
        Dentist dentist = mock(Dentist.class);
        when(dentistRepo.findByUserId(user.getId())).thenReturn(Optional.of(dentist));
        when(dentist.getDentistId()).thenReturn(DentistId.of(1L));
        when(dentistValidator.validate(any())).thenReturn(Outcome.ok());

        Outcome<Void> result = policy.validate(user);

        assertTrue(result.isSuccess());
        verify(dentistValidator).validate(any());
    }

    @Test
    void shouldSkipDentistWhenAbsent() {
        when(dentistRepo.findByUserId(user.getId())).thenReturn(Optional.empty());

        Outcome<Void> result = policy.validate(user);

        assertTrue(result.isSuccess());
        verifyNoInteractions(dentistValidator);
    }

    @Test
    void shouldValidatePatientWhenPresent() {
        Patient patient = mock(Patient.class);
        when(patientRepo.findByUserId(user.getId())).thenReturn(Optional.of(patient));
        when(patient.getPatientId()).thenReturn( PatientId.of(1L));
        when(patientValidator.validate(any())).thenReturn(Outcome.ok());
        when(patient.validateDeactivation()).thenReturn(Outcome.ok());

        Outcome<Void> result = policy.validate(user);

        assertTrue(result.isSuccess());
        verify(patientValidator).validate(any());
        verify(patient).validateDeactivation();
    }

    @Test
    void shouldSkipGuardianWhenAbsent() {
        when(guardianRepo.findByUserId(user.getId())).thenReturn(Optional.empty());

        Outcome<Void> result = policy.validate(user);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldValidateReceptionistWhenPresent() {
        Receptionist receptionist = mock(Receptionist.class);
        when(receptionistRepo.findByUserId(user.getId())).thenReturn(Optional.of(receptionist));
        when(receptionist.getId()).thenReturn( ReceptionId.of(1L));
        when(receptionValidator.validate(any())).thenReturn(Outcome.ok());

        Outcome<Void> result = policy.validate(user);

        assertTrue(result.isSuccess());
        verify(receptionValidator).validate(any());
    }
}
