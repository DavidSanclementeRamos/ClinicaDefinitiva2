package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.*;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper.ReceptionReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper.ReceptionWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.ReceptionUseCase;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;

import java.time.Instant;

@Service
@Transactional
public class ReceptionApplicationService implements ReceptionUseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionReadMapper  readMapper;
    private final ReceptionWriteMapper writeMapper;
    private final UserAccessValidator userAccessValidator;

    public ReceptionApplicationService(ReceptionRepository receptionRepository, ReceptionReadMapper readMapper, ReceptionWriteMapper writeMapper, UserAccessValidator userAccessValidator) {
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.userAccessValidator = userAccessValidator;
    }

    @Override
    public ReadReceptionistDto findById(Long id) {
        Receptionist reception = receptionRepository.findById(ReceptionId.fromLong(id))
                .orElseThrow(() -> new RuntimeException("Receptionist not found with id " + id));
        return readMapper.toDto(reception);
    }

    @Override
    public Page<PageReceptionistDto> findAll(Pageable pageable) {
        Page<Receptionist> receptions = receptionRepository.findAll(pageable);
        if (receptions.isEmpty()) {
            throw new RuntimeException("No receptionists found");
        }
        return receptions.map(readMapper::pageToDto);
    }

    @Override
    public ReadReceptionistDto save(CreateReceptionistDto dto) {
        Receptionist reception = writeMapper.dtoCreateToReception(dto);
        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserIdentityId.from(Long.valueOf(dto.user())),
                now,
                EntityContext.RECEPTIONIST
        );
        receptionRepository.save(reception);
        return readMapper.toDto(reception);
    }

    @Override
    public ReadReceptionistDto updateContact(UpdateReceptionistContactDto dto, Long id) {
        Receptionist reception = receptionRepository.findById(ReceptionId.fromLong(id))
                .orElseThrow(() -> new RuntimeException("Receptionist not found with id " + id));

        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserIdentityId.from(id),
                now,
                EntityContext.RECEPTIONIST
        );
        writeMapper.dtoUpdateContactToReception(dto, reception);
        receptionRepository.save(reception);
        return readMapper.toDto(reception);
    }

    @Override
    public ReadReceptionistDto updateSensitive(UpdateReceptionistSensitiveDto dto, Long id) {
        Receptionist reception = receptionRepository.findById(ReceptionId.fromLong(id))
                .orElseThrow(() -> new RuntimeException("Receptionist not found with id " + id));
        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserIdentityId.from(id),
                now,
                EntityContext.RECEPTIONIST);
        writeMapper.dtoUpdateSensitiveToReception(dto, reception);
        receptionRepository.save(reception);
        return readMapper.toDto(reception);
    }

    @Override
    public Page<PageReceptionistDto> findBySector(String sector, Pageable pageable) {
        Page<Receptionist> receptions = receptionRepository.findBySector(sector, pageable);
        if (receptions.isEmpty()) {
            throw new RuntimeException("No receptionists found in sector " + sector);
        }
        return receptions.map(readMapper::pageToDto);
    }

    @Override
    public void deleteById(Long id) {
        if (!receptionRepository.existsById(ReceptionId.fromLong(id))) {
            throw new RuntimeException("Receptionist with id " + id + " not found");
        }
        Instant now = Instant.now();
        userAccessValidator.validateUserCanPerformSensitiveAction(
                UserIdentityId.from(id),
                now,
                EntityContext.RECEPTIONIST);
        receptionRepository.deleteById(ReceptionId.fromLong(id));
    }
}
