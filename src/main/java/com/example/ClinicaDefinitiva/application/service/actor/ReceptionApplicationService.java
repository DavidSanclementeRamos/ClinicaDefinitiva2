package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.*;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper.ReceptionReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper.ReceptionWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.ReceptionUserCase;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.ReceptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;

@Service
@Transactional
public class ReceptionApplicationService implements ReceptionUserCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionReadMapper  readMapper;
    private final ReceptionWriteMapper writeMapper;

    public ReceptionApplicationService(ReceptionRepository receptionRepository, ReceptionReadMapper readMapper, ReceptionWriteMapper writeMapper ) {
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
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
        receptionRepository.save(reception);
        return readMapper.toDto(reception);
    }

    @Override
    public ReadReceptionistDto updateContact(UpdateReceptionistContactDto dto, Long id) {
        Receptionist reception = receptionRepository.findById(ReceptionId.fromLong(id))
                .orElseThrow(() -> new RuntimeException("Receptionist not found with id " + id));
        writeMapper.dtoUpdateContactToReception(dto, reception);
        receptionRepository.save(reception);
        return readMapper.toDto(reception);
    }

    @Override
    public ReadReceptionistDto updateSensitive(UpdateReceptionistSensitiveDto dto, Long id) {
        Receptionist reception = receptionRepository.findById(ReceptionId.fromLong(id))
                .orElseThrow(() -> new RuntimeException("Receptionist not found with id " + id));
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
        receptionRepository.deleteById(ReceptionId.fromLong(id));
    }
}
