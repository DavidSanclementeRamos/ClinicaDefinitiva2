package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.GuardianUserCase;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.GuardianNoFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.UserRepository;
import com.example.ClinicaDefinitiva.domain.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class GuardianApplicationService implements GuardianUserCase {
    private final GuardianRepository guardianRepository;
    private final GuardianReadMapper readMapper;
    private final GuardianWriteMapper  writeMapper;
    private final UserAccessValidator userAccessValidator;

    public GuardianApplicationService(GuardianRepository guardianRepository, GuardianReadMapper readMapper, GuardianWriteMapper writeMapper, UserAccessValidator userAccessValidator) {
        this.guardianRepository = guardianRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.userAccessValidator = userAccessValidator;
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
        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(createGuardianDto.userId()),
                now,
                EntityContext.GUARDIAN  // Contexto para errores más descriptivos
        );
        Guardian guardian = writeMapper.dtoCreateToGuardian(createGuardianDto);
        guardianRepository.save(guardian);
        return readMapper.toDto(guardian);
    }

    @Override
    public ReadGuardianDto updateContactData(UpdateGuardianContactDto updateGuardian, Long id) {
        Guardian guardian = guardianRepository.findById(GuardianId.fromLong(id))
                .orElseThrow(() -> new GuardianNoFoundException("Guardian not found with id " + id));

        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(id),
                now,
                EntityContext.GUARDIAN  // Contexto para errores más descriptivos
        );
        writeMapper.dtoUpdateContactToGuardian(updateGuardian, guardian);
        guardianRepository.save(guardian);
        return readMapper.toDto(guardian);
    }


    @Override
    public ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto updateGuardian, Long id) {
        Guardian guardian = guardianRepository.findById(GuardianId.fromLong(id))
                .orElseThrow(() -> new GuardianNoFoundException("Guardian not found with id " + id));

        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(id),
                now,
                EntityContext.GUARDIAN
        );
        writeMapper.dtoUpdateSensitiveToGuardian(updateGuardian, guardian);
        guardianRepository.save(guardian);
        return readMapper.toDto(guardian);
    }

    @Override
    public void deleteById(Long id) {
        if (!guardianRepository.existsById(GuardianId.fromLong(id))) {
            throw new GuardianNoFoundException("Guardian with id " + id + " not found");
        }
        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserId.from(id),
                now,
                EntityContext.GUARDIAN
        );
        guardianRepository.deleteById(GuardianId.fromLong(id));
    }
}
