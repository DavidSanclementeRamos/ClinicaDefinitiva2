package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.patient;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.PagePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient.PagePatientResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient.ReadPatientResponse;
import org.springframework.stereotype.Component;

@Component
public class PatientRestReadMapper {

    // De DTO de aplicación → DTO REST completo (detalle)
    public ReadPatientResponse toRest(ReadPatientDto dto) {
        if (dto == null) return null;

        return new ReadPatientResponse(
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
                dto.userId(),              // Consistente: userId
                dto.lastUpdate(),
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.postalCode()
        );
    }

    // De DTO de aplicación → DTO REST simplificado
    public PagePatientResponse toPageRest(PagePatientDto dto) {
        if (dto == null) return null;

        return new PagePatientResponse(
                dto.patientId(),
                dto.contractId(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.phoneNumber()
        );
    }
}