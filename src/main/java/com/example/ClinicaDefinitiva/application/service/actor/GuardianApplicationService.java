package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.GuardianUserCase;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.GuardianNoFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuardianApplicationService implements GuardianUserCase {
    private final GuardianRepository guardianRepository;
    private final GuardianReadMapper readMapper;
    private final GuardianWriteMapper  writeMapper;

    public GuardianApplicationService(GuardianRepository guardianRepository, GuardianReadMapper readMapper, GuardianWriteMapper writeMapper ) {
        this.guardianRepository = guardianRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }


    @Override
    public ReadGuardianDto findById(Long id) {
        Guardian guardian = guardianRepository.findById(GuardianId.fromLong(id))
                .orElseThrow(() -> new GuardianNoFoundException("Guardian not found with id " + id));
        return readMapper.toDto(guardian);
    }

    @Override
    public Page<PageGuardianDto> findAll(Pageable pageable) {
        Page<Guardian> guardians = guardianRepository.findAll(pageable);
        if (guardians.isEmpty()) {
            throw new GuardianNoFoundException("No guardians found");
        }
        return guardians.map(readMapper::pageToDto);
    }

    @Override
    public Page<PageGuardianDto> findByPatientId(Long patientId, Pageable pageable) {
        Page<Guardian> guardian  = guardianRepository.findByPatientId(PatientId.fromLong(patientId), pageable);
        if(guardian.isEmpty()) {
            throw new GuardianNoFoundException("No guardians found for patient with id " + patientId);
        }
        return guardian.map(readMapper::pageToDto);

    }

    @Override
    public ReadGuardianDto save(CreateGuardianDto createGuardianDto) {
        Guardian guardian = writeMapper.dtoCreateToGuardian(createGuardianDto);
        guardianRepository.save(guardian);
        return readMapper.toDto(guardian);
    }

    @Override
    public ReadGuardianDto updateContactData(UpdateGuardianContactDto updateGuardian, Long id) {
        Guardian guardian = guardianRepository.findById(GuardianId.fromLong(id))
                .orElseThrow(() -> new GuardianNoFoundException("Guardian not found with id " + id));

        writeMapper.dtoUpdateContactToGuardian(updateGuardian, guardian);
        guardianRepository.save(guardian);
        return readMapper.toDto(guardian);
    }


    @Override
    public ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto updateGuardian, Long id) {
        Guardian guardian = guardianRepository.findById(GuardianId.fromLong(id))
                .orElseThrow(() -> new GuardianNoFoundException("Guardian not found with id " + id));

        writeMapper.dtoUpdateSensitiveToGuardian(updateGuardian, guardian);
        guardianRepository.save(guardian);
        return readMapper.toDto(guardian);
    }

    @Override
    public void deleteById(Long id) {
        if (!guardianRepository.existsById(GuardianId.fromLong(id))) {
            throw new GuardianNoFoundException("Guardian with id " + id + " not found");
        }
        guardianRepository.deleteById(GuardianId.fromLong(id));
    }
}
