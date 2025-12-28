package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.CreateDentistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.dentist.ReadDentistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.dentist.UpdateDentistDto;
import com.example.ClinicaDefinitiva.application.mapper.DentistMapper;
import com.example.ClinicaDefinitiva.application.usecase.DentistUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.portsInput.actorRepository.DentistRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DentistApplicationService implements DentistUseCase {
    private final DentistRepository dentistRepository;
    private final UserRepository userRepository;
    private final DentistMapper dentistMapper;

    public DentistApplicationService(DentistRepository dentistRepository, UserRepository userRepository, DentistMapper dentistMapper) {
        this.dentistRepository = dentistRepository;
        this.userRepository = userRepository;
        this.dentistMapper = dentistMapper;
    }

    @Override
    public ReadDentistDto findById(Long id) {
        DentistId dentistId = DentistId.fromString(String.valueOf(id));
        Dentist dentist = dentistRepository.findById(dentistId).
                orElseThrow(() -> new IllegalArgumentException(" Dentist no found"));
        return dentistMapper.toDentist(dentist);
    }

    @Override
    public Page<ReadDentistDto> findAll(Pageable pageable) {
        Page<Dentist> dentists = dentistRepository.findAll(pageable);
        if(dentists.isEmpty()){
            throw new IllegalArgumentException("No found");
        }
        return dentists.map(dentistMapper::toDentist);
    }

    @Override
    public ReadDentistDto save(CreateDentistDto createDentistDto) {
       // conversion de string a vo
        UserId userId = UserId.fromString(createDentistDto.getUser());
        // verificar existencia de user
        UserIdentity user = userRepository.findById((userId)).
                orElseThrow(() -> new IllegalArgumentException("No found"));
        Dentist dentist = Dentist.registerDentist(
                createDentistDto.getDentistId(),
                createDentistDto.getPersonData(),
                createDentistDto.getSpecialties(),
                user,
                createDentistDto.getAvailability(),
                createDentistDto.getLastUpdate()
        );
         dentistRepository.save(dentist);
        return dentistMapper.toDentist(dentist);
    }

    @Override
    public ReadDentistDto updateSensitive(UpdateDentistDto updateDentistDto) {

        DentistId dentistId = DentistId.fromString(updateDentistDto.getId());
        Dentist dentist =  dentistRepository.findById(dentistId)
                .orElseThrow(() -> new IllegalArgumentException("No found" + dentistId));

        // conversion de string a vo
        UserId userId = UserId.fromString(updateDentistDto.getUserId());
        // verificar existencia de user
        UserIdentity user = userRepository.findById((userId)).
                orElseThrow(() -> new IllegalArgumentException("No found"));

        dentist.updateSensitiveData(
                updateDentistDto.getUpdatePersonContactData() ,
                user, updateDentistDto.getSpecialties(),
                updateDentistDto.getWorkingHours());

        dentistRepository.save(dentist);
        return dentistMapper.toDentist(dentist);
    }

    @Override
    public ReadDentistDto updateContact(UpdateDentistDto updateDentistDto) {
        DentistId dentistId = DentistId.fromString(updateDentistDto.getId());
        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new IllegalArgumentException("No found" + dentistId));

        // conversion de string a vo
        UserId userId = UserId.fromString(updateDentistDto.getUserId());
        // verificar existencia de user
        UserIdentity user = userRepository.findById((userId)).
                orElseThrow(() -> new IllegalArgumentException("No found"));

        dentist.updateContactData(updateDentistDto.getUpdatePersonContactData(), user);

       dentistRepository.save(dentist);
       return dentistMapper.toDentist(dentist);
    }
}
