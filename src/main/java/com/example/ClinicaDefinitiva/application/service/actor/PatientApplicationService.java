package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientDto;
import com.example.ClinicaDefinitiva.application.mapper.PatientMapper;
import com.example.ClinicaDefinitiva.application.usecase.PatientUserCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.portsInput.actorRepository.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class PatientApplicationService implements PatientUserCase {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public PatientApplicationService(PatientRepository patientRepository,
                                     PatientMapper patientMapper,
                                     UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ReadPatientDto findById(Long id) {
        PatientId patientId = PatientId.fromString(String.valueOf(id));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: id=" + id));
        return patientMapper.toReadPatientDto(patient);
    }

    @Override
    public Page<ReadPatientDto> findAll(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(patientMapper::toReadPatientDto);
    }

    @Override
    public ReadPatientDto save(CreatePatientDto dto) {
        if (dto == null) throw new IllegalArgumentException("CreatePatientDto no puede ser null");

        PatientId patientId = PatientId.generate(); // o desde dto si ya viene
        UserId userId = UserId.fromString(dto.getUser());
        GuardianId guardianId = GuardianId.fromString(dto.getGuardianId().getVauel());
        ContractId contractId = ContractId.fromString(dto.getContractId().getValue());

        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + userId));

        Person person = dto.getPerson(); // Asumo que CreatePatientDto puede construir Person
        LocalDateTime now = LocalDateTime.now();

        Patient patient = Patient.registerPatient(
                patientId,
                person,
                user,
                guardianId,
                now,
                contractId
        );

        patientRepository.save(patient);
        return patientMapper.toReadPatientDto(patient);
    }

    @Override
    public ReadPatientDto updateContact(UpdatePatientDto dto) {
        if (dto == null) throw new IllegalArgumentException("UpdatePatientDto no puede ser null");

        PatientId patientId = PatientId.fromString(dto.getPatientId().toString());
        UserId userId = UserId.fromString(dto.getUserId());

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: id=" + dto.getPatientId()));

        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + dto.getUserId()));

       patient.updatePatientContact(dto.getPerson(), user);

        patientRepository.save(patient);
        return patientMapper.toReadPatientDto(patient);
    }

    @Override
    public ReadPatientDto updateSensitive(UpdatePatientDto updatePatientDto) {

        PatientId patientId = PatientId.fromString(updatePatientDto.getPatientId().toString());
        UserId userId = UserId.fromString(updatePatientDto.getUserId());

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: id=" + updatePatientDto.getPatientId()));

        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + updatePatientDto.getUserId()));

        patient.updateDataSensible(updatePatientDto.getPerson(), user);

        patientRepository.save(patient);
        return patientMapper.toReadPatientDto(patient);
    }
}