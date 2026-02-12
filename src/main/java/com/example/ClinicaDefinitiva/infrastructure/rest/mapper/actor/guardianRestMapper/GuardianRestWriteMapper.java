package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.guardianRestMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian.CreateGuardianRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian.UpdateGuardianContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian.UpdateGuardianSensitiveRequest;
import org.springframework.stereotype.Component;

@Component
public class GuardianRestWriteMapper {

    // De REST → DTO de aplicación (crear)
    public CreateGuardianDto toServiceCreate(CreateGuardianRequest request) {
        if (request == null) return null;

        return new CreateGuardianDto(
                request.code(),
                request.description(),
                request.patientList(),
                request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.phoneNumber(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentoEPS(),
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
    public UpdateGuardianContactDto toServiceUpdateContact(UpdateGuardianContactRequest request) {
        if (request == null) return null;

        return new UpdateGuardianContactDto(
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber()
        );
    }

    public UpdateGuardianSensitiveDto toServiceUpdateSensitive(UpdateGuardianSensitiveRequest request) {
        if (request == null) return null;

        return new UpdateGuardianSensitiveDto(
               request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentEPS(),
                request.code(), request.description()

        );
    }
}
