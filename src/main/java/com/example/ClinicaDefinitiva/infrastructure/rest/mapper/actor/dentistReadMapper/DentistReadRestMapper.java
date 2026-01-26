package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentistReadMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.PageDentistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.dentist.ReadDentistDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.DentistPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.DentistReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component

public class DentistReadRestMapper {

    // De DTO de aplicación → DTO REST completo (detalle)
    public DentistReadResponse toResponse(ReadDentistDto dto) {
        if (dto == null) return null;

        return new DentistReadResponse(
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
    public DentistPageResponse toPageResponse(PageDentistDto dto) {
        if (dto == null) return null;

        return new DentistPageResponse(
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

