package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentist;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.*;
import org.springframework.stereotype.Component;

@Component
public class DentistRestWriteMapper {
    // De REST → DTO de aplicación (crear)
    public CreateDentistDto toServiceCreate(CreateDentistRequest request) {
        if (request == null) return null;

        return new CreateDentistDto(
                request.specialties(),
                request.availabilityStatus(),

           new WorkingHoursDto(    request.start(),
                request.end(),
                request.dayOfWeek(),
                request.declaredHoursPerWeek()),

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
    public UpdateDentistContactDto toServiceUpdateContact(UpdateDentistContactRequest request) {
        if (request == null) return null;

        return new UpdateDentistContactDto(
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber()
        );
    }

    // De REST → DTO de aplicación (actualizar)
    public UpdateDentistSensitiveDto toServiceUpdateSensitive(UpdateDentistSensitiveRequest request) {
        if (request == null) return null;

        return new UpdateDentistSensitiveDto(
                request.specialties(),
                new WorkingHoursDto(    request.start(),
                request.end(),
                request.dayOfWeek(),
                request.declaredHoursPerWeek()),
                request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentoEPS()
        );

    }
    public UpdateDentistStatusDto toUpdateStatusDto(DentistUpdateStatusRequest request) {
        if (request == null) return null;

        return new UpdateDentistStatusDto(
                request.availabilityStatus()
        );

    }

}
