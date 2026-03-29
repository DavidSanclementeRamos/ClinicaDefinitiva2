package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.dentist;

import com.example.ClinicaDefinitiva.application.actor.dto.dentist.CreateDentistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistStatusDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.WorkingHoursDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist.CreateDentistRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist.DentistUpdateStatusRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist.UpdateDentistContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist.UpdateDentistSensitiveRequest;
import org.springframework.stereotype.Component;

@Component
public class DentistRestWriteMapper {
    
    // De REST → DTO de aplicación (crear)
    public CreateDentistDto toServiceCreate(CreateDentistRequest request) {
        if (request == null) return null;

        return new CreateDentistDto(
                request.specialties(),
                request.availabilityStatus(),
                new WorkingHoursDto(
                    request.WorkingHours().start(),
                    request.WorkingHours().end(),
                    request.WorkingHours().dayOfWeek(),
                    request.WorkingHours().declaredHoursPerWeek()
                ),
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

    // De REST → DTO de aplicación (actualizar contacto)
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

    // De REST → DTO de aplicación (actualizar datos sensibles) - CORREGIDO
    public UpdateDentistSensitiveDto toServiceUpdateSensitive(UpdateDentistSensitiveRequest request) {
        if (request == null) return null;

        return new UpdateDentistSensitiveDto(
                request.specialties(),
                new WorkingHoursDto(
                    request.workingHours().start(),
                    request.workingHours().end(),
                    request.workingHours().dayOfWeek(),
                    request.workingHours().declaredHoursPerWeek()
                ),
                request.dni(),
                request.first(),
                request.lastName(),
                request.age(),
                request.dateOfBirth(),
                request.bloodType(),
                request.documentoEPS()
        );
    }
    
    // De REST → DTO de aplicación (actualizar estado)
    public UpdateDentistStatusDto toUpdateStatusDto(DentistUpdateStatusRequest request) {
        if (request == null) return null;

        return new UpdateDentistStatusDto(
                request.availabilityStatus()
        );
    }
}