package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuardianUseCase {


    ReadGuardianDto findById(Long id, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageGuardianDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageGuardianDto> findByPatientId(Long patientId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);


    ReadGuardianDto save(CreateGuardianDto createGuardianDto, UserIdentityId requesterId, RolId requesterRolId);
    ReadGuardianDto updateContactData(UpdateGuardianContactDto updateGuardian, Long id, UserIdentityId requesterId, RolId requesterRolId);
    ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto updateGuardian, Long id, UserIdentityId requesterId, RolId requesterRolId);


    void deleteById(Long id, UserIdentityId requesterId, RolId requesterRolId);
}
