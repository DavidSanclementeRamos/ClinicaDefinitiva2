package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper;


import com.example.ClinicaDefinitiva.application.dto.actor.Patient.PagePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.PatientPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.PatientReadResponse;
import org.springframework.stereotype.Component;

@Component
public class PatientReadMapperRest {

    // De DTO de aplicación → DTO REST completo (detalle)
    public PatientReadResponse toResponse(ReadPatientDto dto) {
        if (dto == null) return null;

        return new PatientReadResponse(
                dto.patientId(),
                dto.guardianId(),
                dto.contractId(),
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
    public PatientPageResponse toPageResponse(PagePatientDto dto) {
        if (dto == null) return null;

        return new PatientPageResponse(
                dto.patientId(),
                dto.contractId(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.phoneNumber()
        );
    }
}
