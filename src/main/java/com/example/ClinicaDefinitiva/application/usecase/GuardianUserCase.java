package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.dentist.UpdateDentistDto;
import com.example.ClinicaDefinitiva.application.dto.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.guardian.ReadGuardian;
import com.example.ClinicaDefinitiva.application.dto.guardian.UpdateGuardian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuardianUserCase {
    ReadGuardian findById(Long id);
    Page<ReadGuardian> findAll(Pageable pageable);
    ReadGuardian save(CreateGuardianDto createGuardianDto);
    ReadGuardian updateContact(UpdateGuardian updateGuardian);
    ReadGuardian updateSensitive(UpdateGuardian updateGuardian);

}
