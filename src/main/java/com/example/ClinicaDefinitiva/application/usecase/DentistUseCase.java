package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.CreateDentistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.dentist.ReadDentistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.dentist.UpdateDentistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistUseCase {
    ReadDentistDto findById(Long id);
    Page<ReadDentistDto> findAll(Pageable pegeable );
    ReadDentistDto save(CreateDentistDto createDentistDto);
    ReadDentistDto updateContact(UpdateDentistDto updateDentistDto);
    ReadDentistDto updateSensitive(UpdateDentistDto updateDentistDto);



}
