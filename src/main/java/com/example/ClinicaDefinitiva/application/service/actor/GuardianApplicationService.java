package com.example.ClinicaDefinitiva.application.service.actor;


import com.example.ClinicaDefinitiva.application.dto.actor.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.ReadGuardian;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardian;
import com.example.ClinicaDefinitiva.application.mapper.GuardianMapper;
import com.example.ClinicaDefinitiva.application.usecase.GuardianUserCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.portsInput.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GuardianApplicationService implements GuardianUserCase {
    private final UserRepository userRepository;
    private final GuardianRepository guardianRepository;
    private final GuardianMapper guardianMapper;
    private final PatientRepository patientRepository;

    public GuardianApplicationService(UserRepository userRepository, GuardianRepository guardianRepository, GuardianMapper guardianMapper, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.guardianRepository = guardianRepository;
        this.guardianMapper = guardianMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public ReadGuardian findById(Long id) {
        GuardianId guardianId = GuardianId.fromString(String.valueOf(id));
        Guardian guardian = guardianRepository.findById(guardianId)
                .orElseThrow(() -> new IllegalArgumentException(" No found"));
        return guardianMapper.toGuardian(guardian);
    }

    @Override
    public Page<ReadGuardian> findAll(Pageable pageable) {
        Page<Guardian> guardianPage = guardianRepository.findAll(pageable);
        if(guardianPage.isEmpty()){
            throw new IllegalArgumentException("List empty");
        }

        return guardianPage.map(guardianMapper::toGuardian);
    }

    @Override
    public ReadGuardian save(CreateGuardianDto createGuardianDto) {
        // conversion de string a vo
        UserId userId = UserId.fromString(createGuardianDto.getUserId());
        // verificar existencia de user
        UserIdentity user = userRepository.findById((userId)).
                orElseThrow(() -> new IllegalArgumentException("No found"));
       // conversion
        GuardianId id = GuardianId.fromString(createGuardianDto.getGuardianId());
        Guardian guardian = Guardian.registerGuardian(
               id,
                createGuardianDto.getPerson(),
                user,
                createGuardianDto.getTypeGuardian()

        );
        guardianRepository.save(guardian);
        return guardianMapper.toGuardian(guardian);
    }

    @Override
    public ReadGuardian updateContact(UpdateGuardian updateGuardian) {
       GuardianId id = GuardianId.fromString(updateGuardian.getGuardianId());
        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Guardian no found"));

        UserId userId = UserId.fromString(updateGuardian.getUserId());
        // verificar existencia de user
        UserIdentity user = userRepository.findById((userId)).
                orElseThrow(() -> new IllegalArgumentException("No found"));

        guardian.updateContactData(updateGuardian.getData(),user);
       guardianRepository.save(guardian);
        return guardianMapper.toGuardian(guardian);
    }

    @Override
    public ReadGuardian updateSensitive(UpdateGuardian updateGuardian) {
        GuardianId id = GuardianId.fromString(updateGuardian.getGuardianId());
        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Guardian no found"));

        UserId userId = UserId.fromString(updateGuardian.getUserId());
        // verificar existencia de user
        UserIdentity user = userRepository.findById((userId)).
                orElseThrow(() -> new IllegalArgumentException("No found"));

        guardian.updateSensitiveData(updateGuardian.getData(),user, updateGuardian.getTypeGuardian());
        guardianRepository.save(guardian);
        return guardianMapper.toGuardian(guardian);
    }


}
