package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.CreatePatientRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.UpdatePatientContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.UpdatePatientSensitiveRequest;
import org.springframework.stereotype.Component;

@Component
public class PatientRestWriteMapper {

    // De REST → DTO de aplicación (crear)
    public CreatePatientDto toServiceCreate(CreatePatientRequest request) {
        if (request == null) return null;

        return new CreatePatientDto(
                request.guardianId(),
                request.contractId(),
                request.user(),
                request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.phoneNumber(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentEPS(),
                request.lastUpdate(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode()
        );
    }

    // De REST → DTO de aplicación (actualizar)
    public UpdatePatientContactDto toServiceUpdateContact(UpdatePatientContactRequest request) {
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

    public UpdatePatientSensitiveDto toServiceUpdateSensitive(UpdatePatientSensitiveRequest request) {
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
