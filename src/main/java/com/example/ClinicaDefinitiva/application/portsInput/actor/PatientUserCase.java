package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientUserCase {
    ReadPatientDto findById(Long id);
    Page<PagePatientDto> findAll(Pageable pageable);
    ReadPatientDto save(CreatePatientDto createPatientDto);
    ReadPatientDto updateContactData(UpdatePatientContactDto updatePatientDto, Long id);
    ReadPatientDto updateSensitiveData(UpdatePatientSensitiveDto updatePatientDto, Long id);
    Page<PagePatientDto> findByContractId(Long contractId, Pageable pageable);
    Page<PagePatientDto> findByGuardianId(Long guardianId, Pageable pageable);
    void deleteById(Long id);

}
