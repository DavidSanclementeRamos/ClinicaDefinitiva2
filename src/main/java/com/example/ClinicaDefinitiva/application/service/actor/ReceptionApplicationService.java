package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.receptionist.CreateReceptionDto;
import com.example.ClinicaDefinitiva.application.dto.receptionist.ReadReceptionDto;
import com.example.ClinicaDefinitiva.application.dto.receptionist.UpdateReceptionDto;
import com.example.ClinicaDefinitiva.application.mapper.ReceptionMapper;
import com.example.ClinicaDefinitiva.application.usecase.ReceptionUserCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.ReceptionId;
import com.example.ClinicaDefinitiva.domain.identity.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.identity.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.portsInput.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class ReceptionApplicationService implements ReceptionUserCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionMapper receptionMapper;
    private final UserRepository userRepository;

    public ReceptionApplicationService(ReceptionRepository receptionRepository,
                                       ReceptionMapper receptionMapper,
                                       UserRepository userRepository) {
        this.receptionRepository = receptionRepository;
        this.receptionMapper = receptionMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ReadReceptionDto findById(String id) {
        ReceptionId receptionId = ReceptionId.fromString(id);
        Receptionist r = receptionRepository.findById(receptionId)
                .orElseThrow(() -> new IllegalArgumentException("Receptionist not found: " + id));
        return receptionMapper.toReadReceptionDto(r);
    }

    @Override
    public Page<ReadReceptionDto> findAll(Pageable pageable) {
        return receptionRepository.findAll(pageable).map(receptionMapper::toReadReceptionDto);
    }

    @Override
    public ReadReceptionDto save(CreateReceptionDto dto) {
        if (dto == null) throw new IllegalArgumentException("dto no puede ser null");

        UserId userId = UserId.fromString(dto.getUserId());
        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));
        ReceptionId receptionId = ReceptionId.fromString(dto.getReceptionId());

        Receptionist created = Receptionist.registerReceptionist(receptionId, dto.getPersonData(), user, dto.getSector());

        receptionRepository.save(created);
        return receptionMapper.toReadReceptionDto(created);
    }


    @Override
    public ReadReceptionDto updateContact(UpdateReceptionDto dto) {
        if (dto == null) throw new IllegalArgumentException("dto no puede ser null");

        ReceptionId receptionId = ReceptionId.fromString(dto.getReceptionId());
        Receptionist existing = receptionRepository.findById(receptionId)
                .orElseThrow(() -> new IllegalArgumentException("Receptionist not found: " + dto.getReceptionId()));

        UserId userId = UserId.fromString(dto.getUserId());
        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));


        existing.updateReceptionistContactData(dto.getPersonData(), user);

        receptionRepository.save(existing);
        return receptionMapper.toReadReceptionDto(existing);
    }

    @Override
    public ReadReceptionDto updateSensitive(UpdateReceptionDto dto) {
        if (dto == null) throw new IllegalArgumentException("dto no puede ser null");

        ReceptionId receptionId = ReceptionId.fromString(dto.getReceptionId());
        Receptionist existing = receptionRepository.findById(receptionId)
                .orElseThrow(() -> new IllegalArgumentException("Receptionist not found: " + dto.getReceptionId()));

        UserId userId = UserId.fromString(dto.getUserId());
        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));

        existing.updateReceptionistSensitiveData( dto.getPersonData(), user, dto.getSector());

        receptionRepository.save(existing);
        return receptionMapper.toReadReceptionDto(existing);
    }
}
