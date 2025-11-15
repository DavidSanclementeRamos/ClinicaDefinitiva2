package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.receptionist.CreateReceptionDto;
import com.example.ClinicaDefinitiva.application.dto.receptionist.ReadReceptionDto;
import com.example.ClinicaDefinitiva.application.dto.receptionist.UpdateReceptionDto;
import com.example.ClinicaDefinitiva.application.dto.receptionist.updateContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceptionUserCase {
    ReadReceptionDto findById(String id);
    Page<ReadReceptionDto> findAll(Pageable pageable);
    ReadReceptionDto save(CreateReceptionDto dto);
    ReadReceptionDto updateContact ( UpdateReceptionDto dto );
    ReadReceptionDto updateSensitive(UpdateReceptionDto dto);

}
