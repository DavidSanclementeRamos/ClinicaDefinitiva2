package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.patient;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient.CreatePatientRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient.UpdatePatientContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient.UpdatePatientSensitiveRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PatientRestWriteMapper {

    // De REST → DTO de aplicación (crear)
    public CreatePatientDto toServiceCreate(CreatePatientRequest request) {
        if (request == null) return null;

        return new CreatePatientDto(
                request.guardianId(),
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


    public UpdatePatientContactDto toServiceUpdateContact(UpdatePatientContactRequest request) {
    if (request == null) return null;
    return new UpdatePatientContactDto(
        Optional.ofNullable(request.street()),
        Optional.ofNullable(request.city()),
        Optional.ofNullable(request.state()),
        Optional.ofNullable(request.country()),
        Optional.ofNullable(request.postalCode()),
        Optional.ofNullable(request.phoneNumber())
    );
}

public UpdatePatientSensitiveDto toServiceUpdateSensitive(UpdatePatientSensitiveRequest request) {
    if (request == null) return null;
    return new UpdatePatientSensitiveDto(
        Optional.ofNullable(request.dni()),
        Optional.ofNullable(request.first()),
        Optional.ofNullable(request.lastName()),
        Optional.ofNullable(request.age()),
        Optional.ofNullable(request.dateOfBirth()),
        Optional.ofNullable(request.bloodType()),
        Optional.ofNullable(request.documentEPS())
    );
}
    
}
