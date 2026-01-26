package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.*;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper.PatientReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper.PatientWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.PatientUserCase;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.PatientNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientApplicationService implements PatientUserCase {

    private final PatientRepository patientRepository;
    private final PatientReadMapper readMapper ;
    private final PatientWriteMapper writeMapper ;

    public PatientApplicationService(PatientRepository patientRepository, PatientReadMapper readMapper, PatientWriteMapper writeMapper) {
        this.patientRepository = patientRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
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
        Patient patient = writeMapper.dtoCreateToPatient(createPatientDto);
        patientRepository.save(patient);
        return readMapper.toDto(patient);
    }

    @Override
    public ReadPatientDto updateContactData(UpdatePatientContactDto updatePatientDto, Long id) {
        Patient patient = patientRepository.findById(PatientId.fromLong(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));

        writeMapper.dtoUpdateContactToPatient(updatePatientDto, patient);
        patientRepository.save(patient);
        return readMapper.toDto(patient);
    }



    @Override
    public ReadPatientDto updateSensitiveData(UpdatePatientSensitiveDto updatePatientDto, Long id) {
        Patient patient = patientRepository.findById(PatientId.fromLong(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));

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
        patientRepository.deleteById(PatientId.fromLong(id));
    }
}