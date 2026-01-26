package com.example.ClinicaDefinitiva.application.portsInput.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceptionUserCase {
    ReadReceptionistDto findById(Long id);
    Page<PageReceptionistDto> findAll(Pageable pageable);
    ReadReceptionistDto save(CreateReceptionistDto dto);
    ReadReceptionistDto updateContact ( UpdateReceptionistContactDto dto , Long id);
    ReadReceptionistDto updateSensitive(UpdateReceptionistSensitiveDto dto, Long id);
    Page<PageReceptionistDto> findBySector(String sector, Pageable pageable);
    void deleteById(Long id);

}
