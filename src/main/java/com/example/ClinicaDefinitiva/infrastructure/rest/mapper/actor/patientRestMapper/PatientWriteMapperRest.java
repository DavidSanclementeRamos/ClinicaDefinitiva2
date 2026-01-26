package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.PatientCreateRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.PatientUpdateContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.PatientUpdateSensitiveRequest;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteMapperRest {

    // De REST → DTO de aplicación (crear)
    public CreatePatientDto toCreateDto(PatientCreateRequest request) {
        if (request == null) return null;

        return new CreatePatientDto(
                request.guardianId(),
                request.contractId(),
                request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.phoneNumber(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentEPS(),
                request.user(),
                request.lastUpdate(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode()
        );
    }

    // De REST → DTO de aplicación (actualizar)
    public UpdatePatientContactDto toUpdateContactDto(PatientUpdateContactRequest request) {
        if (request == null) return null;

        return new UpdatePatientContactDto(
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber()
        );
    }

    public UpdatePatientSensitiveDto toUpdateSensitiveDto(PatientUpdateSensitiveRequest request) {
        if (request == null) return null;

        return new UpdatePatientSensitiveDto(
                request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentEPS()

        );
    }
}
