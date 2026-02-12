package com.example.ClinicaDefinitiva.application.portsInput.actor;


import com.example.ClinicaDefinitiva.application.dto.actor.Patient.*;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientUseCase {


    ReadPatientDto findById(PatientId id, UserIdentityId requesterId, RolId requesterRolId);
    Page<PagePatientDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PagePatientDto> findByContractId(Long contractId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PagePatientDto> findByGuardianId(Long guardianId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);


    ReadPatientDto save(CreatePatientDto createPatientDto, UserIdentityId requesterId, RolId requesterRolId);
    ReadPatientDto updateContactData(UpdatePatientContactDto updatePatientDto, PatientId id, UserIdentityId requesterId, RolId requesterRolId);
    ReadPatientDto updateSensitiveData(UpdatePatientSensitiveDto updatePatientDto, PatientId id, UserIdentityId requesterId, RolId requesterRolId);


    void deleteById(PatientId id, UserIdentityId requesterId, RolId requesterRolId);
}
