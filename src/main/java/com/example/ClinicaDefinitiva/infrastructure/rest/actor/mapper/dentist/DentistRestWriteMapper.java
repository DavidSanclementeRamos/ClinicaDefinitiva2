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
import java.util.Optional;
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

   public UpdateDentistContactDto toServiceUpdateContact(UpdateDentistContactRequest request) {
    if (request == null) return null;
    return new UpdateDentistContactDto(
        Optional.ofNullable(request.street()),
        Optional.ofNullable(request.city()),
        Optional.ofNullable(request.state()),
        Optional.ofNullable(request.country()),
        Optional.ofNullable(request.postalCode()),
        Optional.ofNullable(request.phoneNumber())
    );
}

public UpdateDentistSensitiveDto toServiceUpdateSensitive(UpdateDentistSensitiveRequest request) {
    if (request == null) return null;
    return new UpdateDentistSensitiveDto(
        Optional.ofNullable(request.specialties()),
        Optional.ofNullable(request.workingHours()).map(wh -> new WorkingHoursDto(wh.start(), wh.end(), wh.dayOfWeek(), wh.declaredHoursPerWeek())),
        Optional.ofNullable(request.dni()),
        Optional.ofNullable(request.first()),
        Optional.ofNullable(request.lastName()),
        Optional.ofNullable(request.age()),
        Optional.ofNullable(request.dateOfBirth()),
        Optional.ofNullable(request.bloodType()),
        Optional.ofNullable(request.documentoEPS())
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