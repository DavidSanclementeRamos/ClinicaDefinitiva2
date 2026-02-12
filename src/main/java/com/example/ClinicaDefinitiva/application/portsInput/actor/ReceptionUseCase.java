package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.*;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceptionUseCase {


    ReadReceptionistDto findById(ReceptionId id, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageReceptionistDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageReceptionistDto> findBySector(String sector, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);


    ReadReceptionistDto save(CreateReceptionistDto dto, UserIdentityId requesterId, RolId requesterRolId);
    ReadReceptionistDto updateContact(UpdateReceptionistContactDto dto, ReceptionId id, UserIdentityId requesterId, RolId requesterRolId);
    ReadReceptionistDto updateSensitive(UpdateReceptionistSensitiveDto dto,ReceptionId id, UserIdentityId requesterId, RolId requesterRolId);


    void deleteById(ReceptionId id, UserIdentityId requesterId, RolId requesterRolId);
}

