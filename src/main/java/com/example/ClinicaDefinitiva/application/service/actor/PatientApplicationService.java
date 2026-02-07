package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.*;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper.PatientReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper.PatientWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.PatientUserCase;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.PatientNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class PatientApplicationService implements PatientUserCase {

    private final PatientRepository patientRepository;
    private final PatientReadMapper readMapper ;
    private final PatientWriteMapper writeMapper ;
    private final UserAccessValidator userAccessValidator;


    public PatientApplicationService(PatientRepository patientRepository, PatientReadMapper readMapper, PatientWriteMapper writeMapper, UserAccessValidator userAccessValidator) {
        this.patientRepository = patientRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.userAccessValidator = userAccessValidator;
    }

    @Override
    public ReadPatientDto findById(Long id) {
        Patient patient = patientRepository.findById(PatientId.fromLong(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));
        return readMapper.toDto(patient);
    }

    @Override
    public Page<PagePatientDto> findAll(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("No patients found");
        }
        return patients.map(readMapper::pageToDto);
    }

    @Override
    public ReadPatientDto save(CreatePatientDto createPatientDto) {
        Instant now = Instant.now();

        // PASO 1: Validar acceso del usuario
        // Esta validación lanza excepciones si el usuario no cumple requisitos
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(createPatientDto.userId()),
                now,
                EntityContext.USUARIO  // Contexto para errores más descriptivos
        );
        Patient patient = writeMapper.dtoCreateToPatient(createPatientDto);
        patientRepository.save(patient);
        return readMapper.toDto(patient);
    }

    @Override
    public ReadPatientDto updateContactData(UpdatePatientContactDto updatePatientDto, Long id) {

        Patient patient = patientRepository.findById(PatientId.fromLong(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));

        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(id),
                now,
                EntityContext.PATIENT
        );

        writeMapper.dtoUpdateContactToPatient(updatePatientDto, patient);
        patientRepository.save(patient);
        return readMapper.toDto(patient);
    }



    @Override
    public ReadPatientDto updateSensitiveData(UpdatePatientSensitiveDto updatePatientDto, Long id) {
        Patient patient = patientRepository.findById(PatientId.fromLong(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));

        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(id),
                now,
                EntityContext.PATIENT
        );
        writeMapper.dtoUpdateSensitiveToPatient(updatePatientDto, patient);
        patientRepository.save(patient);
        return readMapper.toDto(patient);
    }

    @Override
    public Page<PagePatientDto> findByContractId(Long contractId, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByContractId(ContractId.fromLong(contractId), pageable);
        if(patients.isEmpty()) {
            throw new PatientNotFoundException("No patients found for contract id " + contractId);
        }
        return patients.map(readMapper::pageToDto);
    }

    @Override
    public Page<PagePatientDto> findByGuardianId(Long guardianId, Pageable pageable) {

        Page<Patient> patients = patientRepository.findByGuardianId(GuardianId.fromLong(guardianId), pageable);
        if(patients.isEmpty()) {
            throw new PatientNotFoundException("No patients found for guardian id " + guardianId);
        }
        return patients.map(readMapper::pageToDto);

    }

    @Override
    public void deleteById(Long id) {
        if (!patientRepository.existsById(PatientId.fromLong(id))) {
            throw new PatientNotFoundException("Patient with id " + id + " not found");
        }

        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(id),
                now,
                EntityContext.PATIENT  // Contexto para errores más descriptivos
        );
        patientRepository.deleteById(PatientId.fromLong(id));
    }
}