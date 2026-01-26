package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuardianUserCase {
    ReadGuardianDto findById(Long id);
    Page<PageGuardianDto> findAll(Pageable pageable);
    Page<PageGuardianDto> findByPatientId(Long patientId, Pageable pageable);
    ReadGuardianDto save(CreateGuardianDto createGuardianDto);
    ReadGuardianDto updateContactData(UpdateGuardianContactDto updateGuardian, Long id);
    ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto updateGuardian, Long id);

    void deleteById(Long id);

}
