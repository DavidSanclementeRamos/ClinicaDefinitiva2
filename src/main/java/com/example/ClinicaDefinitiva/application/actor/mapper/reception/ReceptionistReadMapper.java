package com.example.ClinicaDefinitiva.application.actor.mapper.reception;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.ReadReceptionistDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistReadMapper {

    // dominio → DTO de lectura
    public ReadReceptionistDto toReadDto(Receptionist reception) {
        return new ReadReceptionistDto(
                reception.getId().getValue(),

                reception.getSector().toString(),
                reception.getPerson().getDni().toString(),
                reception.getPerson().getFullname().getFirstName(),
                reception.getPerson().getFullname().getLastName(),
                reception.getPerson().getAge().toString(),

                reception.getPerson().getPhoneNumber().toString(),

                reception.getPerson().getDateOfBirth().asDate(),
                reception.getPerson().getBloodType().getValue(),
                reception.getPerson().getDocumentoEPS(),
                reception.getId().getValue(),
                reception.getLastUpdate(),

                reception.getPerson().getAddress().Street(),
                reception.getPerson().getAddress().City(),
                reception.getPerson().getAddress().State(),
                reception.getPerson().getAddress().Country(),
                reception.getPerson().getAddress().PostalCode());
    }

    // dominio → DTO de lectura resumido (ej. para paginación)
    public PageReceptionistDto toPageDto(Receptionist reception) {
        return new PageReceptionistDto(
                reception.getId().getValue(),
                                reception.getSector().getDescription(),

                reception.getPerson().getDni().toString(),
                reception.getPerson().getFullname().getFirstName(),
                reception.getPerson().getFullname().getLastName(),
                reception.getPerson().getPhoneNumber().toString()
        );
    }
}

