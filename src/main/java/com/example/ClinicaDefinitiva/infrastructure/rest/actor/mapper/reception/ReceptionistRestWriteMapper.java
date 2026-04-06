package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.reception;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.CreateReceptionistRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.UpdateReceptionistContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.UpdateReceptionistSensitiveRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistRestWriteMapper {

    // De REST → DTO de aplicación (crear)
    public CreateReceptionistDto toServiceCreate(CreateReceptionistRequest request) {
        if (request == null) return null;

        return new CreateReceptionistDto(
                request.sector(),
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

   public UpdateReceptionistContactDto toServiceUpdateContact(UpdateReceptionistContactRequest request) {
    if (request == null) return null;
    return new UpdateReceptionistContactDto(
        Optional.ofNullable(request.street()),
        Optional.ofNullable(request.city()),
        Optional.ofNullable(request.state()),
        Optional.ofNullable(request.country()),
        Optional.ofNullable(request.postalCode()),
        Optional.ofNullable(request.phoneNumber())
    );
}

public UpdateReceptionistSensitiveDto toServiceUpdateSensitive(UpdateReceptionistSensitiveRequest request) {
    if (request == null) return null;
    return new UpdateReceptionistSensitiveDto(
        Optional.ofNullable(request.dni()),
        Optional.ofNullable(request.first()),
        Optional.ofNullable(request.lastName()),
        Optional.ofNullable(request.age()),
        Optional.ofNullable(request.dateOfBirth()),
        Optional.ofNullable(request.bloodType()),
        Optional.ofNullable(request.documentEPS()),
        Optional.ofNullable(request.sector())
    );
}
}
