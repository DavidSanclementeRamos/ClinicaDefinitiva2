package com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.Operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistWriteMapper {

    // DTO de entrada → dominio (VOs/Agregado).
    public Receptionist fromCreateDto(CreateReceptionistDto dto) {

        return Receptionist.registerReceptionist(
                new Person(
                        new Address(dto.street(), dto.city(), dto.state(),
                                dto.country(), dto.postalCode()),
                        new Age(new DateOfBirth(dto.dateOfBirth())),
                        BloodType.fromLabel(dto.bloodType()),
                        new DateOfBirth(dto.dateOfBirth()),
                        new Document(dto.dni()),
                        dto.documentEPS(),
                        new FullName(dto.first(), dto.lastName()),
                        new PhoneNumber(dto.phoneNumber())
                ),
               UserIdentityId.from( dto.user()),
                new Sector(dto.sector()),
                ShiftId.from(dto.shiftId())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void updateSensitiveFromDto(UpdateReceptionistSensitiveDto dto, Receptionist reception) {

        reception.updateSensitiveData(
                new Age(new DateOfBirth(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                new DateOfBirth(dto.dateOfBirth()),
                new Document(dto.dni()),
                dto.documentEPS(),
                new FullName(dto.first(), dto.lastName()),
                new Sector(dto.sector())

        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void updateContactFromDto(UpdateReceptionistContactDto dto, Receptionist reception) {

        reception.updateContactData(
                new Address(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),
                new PhoneNumber(dto.phoneNumber())

        );
    }

}

