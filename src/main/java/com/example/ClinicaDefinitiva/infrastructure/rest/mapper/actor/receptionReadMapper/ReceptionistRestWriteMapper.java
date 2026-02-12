package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.receptionReadMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception.CreateReceptionistRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception.UpdateReceptionistContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception.UpdateReceptionistSensitiveRequest;
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

    // De REST → DTO de aplicación (actualizar)
    public UpdateReceptionistContactDto toServiceUpdateContact(UpdateReceptionistContactRequest request) {
        if (request == null) return null;

        return new UpdateReceptionistContactDto(
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber()
        );
    }

    public UpdateReceptionistSensitiveDto toServiceUpdateSensitive(UpdateReceptionistSensitiveRequest request) {
        if (request == null) return null;

        return new UpdateReceptionistSensitiveDto(
               request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentEPS(),
                request.sector()
        );
    }
}
