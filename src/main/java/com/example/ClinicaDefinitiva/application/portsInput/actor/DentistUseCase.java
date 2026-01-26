package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistUseCase {
    ReadDentistDto findById(Long id);
    Page<PageDentistDto> findAll(Pageable pageable );
    ReadDentistDto save(CreateDentistDto createDentistDto);
    ReadDentistDto updateContactData(UpdateDentistContactDto updateDentistDto, Long id);
    ReadDentistDto updateSensitiveData(UpdateDentistSensitiveDto updateDentistDto, Long id);
    ReadDentistDto updateStatus(UpdateDentistStatusDto updateDentistStatusDto, Long id);
    Page<PageDentistDto> findByAvailability(String status, Pageable pageable);
    Page<PageDentistDto> findBySpecialty(String specialty, Pageable pageable);

    void deleteById(Long id);

}
