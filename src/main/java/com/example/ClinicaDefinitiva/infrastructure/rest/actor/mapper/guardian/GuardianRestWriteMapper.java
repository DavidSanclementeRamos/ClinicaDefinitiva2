package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.guardian;

import com.example.ClinicaDefinitiva.application.actor.dto.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.CreateGuardianRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.UpdateGuardianContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.UpdateGuardianSensitiveRequest;
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
                request.user(),           // Cambiado de user a userId para consistencia
                request.lastUpdate(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode()
        );
    }

    // De REST → DTO de aplicación (actualizar contacto)
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

    // De REST → DTO de aplicación (actualizar datos sensibles) - CORREGIDO
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
                request.code(), 
                request.description()
        );
    }
}