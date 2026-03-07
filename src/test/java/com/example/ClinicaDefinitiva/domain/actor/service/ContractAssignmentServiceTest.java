
package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractAssignmentServiceTest {

    private ContractRepository contractRepository;
    private PatientRepository patientRepository;
    private ContractAssignmentService service;

    private PatientId patientId;
    private ContractId contractId;
    private Patient patient;
    private Contract contract;

    @BeforeEach
    void setUp() {
        contractRepository = mock(ContractRepository.class);
        patientRepository = mock(PatientRepository.class);
        service = new ContractAssignmentService(contractRepository, patientRepository);

        patientId = PatientId.of(1L);
        contractId = ContractId.of(1L);

        patient = mock(Patient.class);
        contract = mock(Contract.class);
    }

    @Test
    void shouldAssignContractToPatientWhenContractIsValid() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contract.isActiveAndValid()).thenReturn(true);

        service.assignContractToPatient(patientId, contractId);

        verify(patient).assignContract(contractId);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldThrowExceptionWhenContractIsInvalid() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contract.isActiveAndValid()).thenReturn(false);

        assertThrows(BusinessRuleViolationException.class,
            () -> service.assignContractToPatient(patientId, contractId));

        verify(patient, never()).assignContract(any());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
            () -> service.assignContractToPatient(patientId, contractId));
    }

    @Test
    void shouldThrowExceptionWhenContractNotFound() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
            () -> service.assignContractToPatient(patientId, contractId));
    }
}
