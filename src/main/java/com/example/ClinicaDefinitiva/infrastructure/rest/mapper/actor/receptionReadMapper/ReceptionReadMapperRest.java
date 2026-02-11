package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.receptionReadMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.ReadReceptionistDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception.ReceptionPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception.ReceptionReadResponse;
import org.springframework.stereotype.Component;

@Component
public class ReceptionReadMapperRest {

    // De DTO de aplicación → DTO REST completo (detalle)
    public ReceptionReadResponse toResponse(ReadReceptionistDto dto) {
        if (dto == null) return null;

        return new ReceptionReadResponse(
                dto.receptionist(),
                dto.sector(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.age(),
                dto.phoneNumber(),
                dto.dateOfBirth(),
                dto.bloodType(),
                dto.documentEPS(),
                //dto.user(),
                dto.receptionist(),

                dto.lastUpdate(),
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.postalCode()
        );
    }

    // De DTO de aplicación → DTO REST simplificado (listados/paginación)
    public ReceptionPageResponse toPageResponse(PageReceptionistDto dto) {
        if (dto == null) return null;

        return new ReceptionPageResponse(
                dto.sector(),
                dto.receptionist(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.phoneNumber()
        );
    }
}
