package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;

import java.util.Optional;
import org.springframework.stereotype.Service;
@Service
public class ContractAssignmentService {

    private final ContractRepository contractRepository;
    private final PatientRepository patientRepository;

    public ContractAssignmentService(ContractRepository contractRepository,
                                     PatientRepository patientRepository) {
        this.contractRepository = contractRepository;
        this.patientRepository = patientRepository;
    }

    public void assignContractToPatient(PatientId patientId, ContractId contractId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        Contract contract = contractRepository.findById(contractId).orElseThrow();

        if (!contract.isActiveAndValid()) {
            throw new BusinessRuleViolationException(
                    PatientError.ERR_PATIENT_CONTRACT_INVALID, EntityContext.PATIENT
            );
        }

        patient.assignContract(contractId);
        patientRepository.save(patient);
    }
}

