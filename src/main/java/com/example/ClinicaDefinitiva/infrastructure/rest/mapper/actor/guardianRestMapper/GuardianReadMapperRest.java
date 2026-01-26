package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.guardianRestMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.PageGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.ReadGuardianDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian.GuardianPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian.GuardianReadResponse;
import org.springframework.stereotype.Component;

@Component
public class GuardianReadMapperRest {

    // De DTO de aplicación → DTO REST completo (detalle)
    public GuardianReadResponse toResponse(ReadGuardianDto dto) {
        if (dto == null) return null;

        return new GuardianReadResponse(
                dto.guardianId(),
                dto.code(),
                dto.description(),
                dto.patientList(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.age(),
                dto.phoneNumber(),
                dto.dateOfBirth(),
                dto.bloodType(),
                dto.documentEPS(),
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
    public GuardianPageResponse toPageResponse(PageGuardianDto dto) {
        if (dto == null) return null;

        return new GuardianPageResponse(
                dto.guardianId(),
                dto.code(),
                dto.description(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.phoneNumber()
        );
    }
}
