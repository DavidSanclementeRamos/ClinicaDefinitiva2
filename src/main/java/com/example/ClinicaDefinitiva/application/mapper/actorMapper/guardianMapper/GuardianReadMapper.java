package com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.PageGuardianDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.ReadGuardianDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GuardianReadMapper {

    // dominio → DTO de lectura
    public ReadGuardianDto toReadDto(Guardian guardian) {
        return new ReadGuardianDto(
                guardian.getGuardianId().getValue(),
                guardian.getTypeGuardian().getCode(),
                guardian.getTypeGuardian().getDescription(),
                Collections.singletonList(guardian.getPatientList().toString()),
                guardian.getPerson().getDni().toString(),
                guardian.getPerson().getFullname().FirstName(),
                guardian.getPerson().getFullname().LastName(),
                guardian.getPerson().getAge().toString(),
                guardian.getPerson().getPhoneNumber().toString(),
                guardian.getPerson().getDateOfBirth().asDate(),
                guardian.getPerson().getBloodType().getValue(),
                guardian.getPerson().getDocumentoEPS(),
                guardian.getUserId().value(),
                guardian.getLastUpdate(),
                guardian.getPerson().getAddress().Street(),
                guardian.getPerson().getAddress().City(),
                guardian.getPerson().getAddress().State(),
                guardian.getPerson().getAddress().Country(),
                guardian.getPerson().getAddress().PostalCode()
                //guardian.getRelationship().toString(),
                //guardian.getLastUpdate()
        );
    }

    // dominio → DTO de lectura resumido (ej. para paginación)
    public PageGuardianDto toPageDto(Guardian guardian) {
        return new PageGuardianDto(
                guardian.getGuardianId().getValue(),
                guardian.getTypeGuardian().getCode(),
                guardian.getTypeGuardian().getDescription(),
                guardian.getPerson().getDni().toString(),
                guardian.getPerson().getFullname().FirstName(),
                guardian.getPerson().getFullname().LastName(),
                guardian.getPerson().getPhoneNumber().toString()
               // guardian.getRelationship().toString()
        );
    }
}

