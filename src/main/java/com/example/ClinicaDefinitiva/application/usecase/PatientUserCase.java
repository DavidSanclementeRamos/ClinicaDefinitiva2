package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.application.dto.Patient.UpdatePatientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientUserCase {
    ReadPatientDto findById(Long id);
    Page<ReadPatientDto> findAll(Pageable pageable);
    ReadPatientDto save(CreatePatientDto createPatientDto);
    ReadPatientDto updateContact(UpdatePatientDto updatePatientDto);
    ReadPatientDto updateSensitive(UpdatePatientDto updatePatientDto);

}
