package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuardianUseCase {


    ReadGuardianDto findById(GuardianId id, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageGuardianDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageGuardianDto> findByPatientId(PatientId patientId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);


    ReadGuardianDto save(CreateGuardianDto createGuardianDto, UserIdentityId requesterId, RolId requesterRolId);
    ReadGuardianDto updateContactData(UpdateGuardianContactDto updateGuardian, GuardianId id, UserIdentityId requesterId, RolId requesterRolId);
    ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto updateGuardian, GuardianId id, UserIdentityId requesterId, RolId requesterRolId);


    void deleteById(GuardianId id, UserIdentityId requesterId, RolId requesterRolId);
}
