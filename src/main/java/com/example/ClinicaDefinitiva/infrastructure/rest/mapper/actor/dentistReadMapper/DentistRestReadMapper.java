package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentistReadMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.PageDentistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.dentist.ReadDentistDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.PageDentistResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.ReadDentistResponse;
import org.springframework.stereotype.Component;

@Component

public class DentistRestReadMapper {

    // De DTO de aplicación → DTO REST completo (detalle)
    public ReadDentistResponse toRest(ReadDentistDto dto) {
        if (dto == null) return null;

        return new ReadDentistResponse(
                dto.dentistId(),
                dto.specialties(),
                dto.availabilityStatus(),
                dto.start(),
                dto.end(),
                dto.dayOfWeek(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.age(),
                dto.phoneNumber(),
                dto.dateOfBirth(),
                dto.bloodType(),
                dto.documentoEPS(),
                dto.user(),
                dto.lastUpdate(),
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.postalCode()
        );
    }

    // De DTO de aplicación → DTO REST simplificado (listados/paginación)
    public PageDentistResponse toPageRest(PageDentistDto dto) {
        if (dto == null) return null;

        return new PageDentistResponse(
                dto.dentistId(),
                dto.specialties(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.phoneNumber(),
                dto.availabilityStatus()
        );
    }



    // Page<ReadDentistDto> → respuesta REST con metadatos
   /** public DentistPageResponse toPageResponse(Page<ReadDentistDto> page) {
        return new DentistPageResponse(
                page.getContent().stream()
                        .map(this::toListResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }*/



}

