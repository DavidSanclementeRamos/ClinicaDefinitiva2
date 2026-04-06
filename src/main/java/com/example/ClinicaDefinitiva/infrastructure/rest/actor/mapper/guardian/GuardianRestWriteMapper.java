package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.guardian;

import com.example.ClinicaDefinitiva.application.actor.dto.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.CreateGuardianRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.UpdateGuardianContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.UpdateGuardianSensitiveRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GuardianRestWriteMapper {

    // De REST → DTO de aplicación (crear)
    public CreateGuardianDto toServiceCreate(CreateGuardianRequest request) {
        if (request == null) return null;

        return new CreateGuardianDto(
                request.code(),
                request.description(),
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
        Optional.ofNullable(request.street()),
        Optional.ofNullable(request.city()),
        Optional.ofNullable(request.state()),
        Optional.ofNullable(request.country()),
        Optional.ofNullable(request.postalCode()),
        Optional.ofNullable(request.phoneNumber())
    );
}

   public UpdateGuardianSensitiveDto toServiceUpdateSensitive(UpdateGuardianSensitiveRequest request) {
    if (request == null) return null;

    return new UpdateGuardianSensitiveDto(
        Optional.ofNullable(request.dni()),
        Optional.ofNullable(request.first()),
        Optional.ofNullable(request.lastName()),
        Optional.ofNullable(request.age()),
        Optional.ofNullable(request.dateOfBirth()),
        Optional.ofNullable(request.bloodType()),
        Optional.ofNullable(request.documentEPS()),
        Optional.ofNullable(request.code()),
        Optional.ofNullable(request.description())
    );
}
}