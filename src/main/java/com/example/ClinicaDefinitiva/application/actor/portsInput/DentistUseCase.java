package com.example.ClinicaDefinitiva.application.actor.portsInput;

import com.example.ClinicaDefinitiva.application.actor.dto.dentist.*;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface DentistUseCase {


    ReadDentistDto findById(DentistId id, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageDentistDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageDentistDto> findBySpecialty(Specialty specialty, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);


    ReadDentistDto save(CreateDentistDto createDentistDto, UserIdentityId requesterId, RolId requesterRolId);
    ReadDentistDto updateContactData(UpdateDentistContactDto updateDentistDto, Long id, UserIdentityId requesterId, RolId requesterRolId);
    ReadDentistDto updateSensitiveData(UpdateDentistSensitiveDto updateDentistDto, Long id, UserIdentityId requesterId, RolId requesterRolId);


    void applyVacation(LocalDateTime start, LocalDateTime end, UserIdentityId requesterId, RolId requesterRolId);
    void applyIncapacity(LocalDateTime start, LocalDateTime end, String note, UserIdentityId requesterId, RolId requesterRolId);
    void returnToAvailable(UserIdentityId requesterId, RolId requesterRolId);


    void deleteById(DentistId id, UserIdentityId requesterId, RolId requesterRolId);
}

