package com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import org.springframework.stereotype.Component;

@Component
public class ReceptionWriteMapper {

    // DTO de entrada → dominio (VOs/Agregado).
    public Receptionist dtoCreateToReception(CreateReceptionistDto dto) {
        UserIdentity user = new UserIdentity();

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
                user,
                new Sector(dto.sector())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateSensitiveToReception(UpdateReceptionistSensitiveDto dto, Receptionist reception) {
        UserIdentity user = new UserIdentity();

        reception.updateSensitiveData(
                new Age(new DateOfBirth(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                new DateOfBirth(dto.dateOfBirth()),
                new Document(dto.dni()),
                dto.documentEPS(),
                new FullName(dto.first(), dto.lastName()),

                user,
                new Sector(dto.sector())

        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateContactToReception(UpdateReceptionistContactDto dto, Receptionist reception) {
        UserIdentity user = new UserIdentity();

        reception.updateContactData(
                new Address(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),
                new PhoneNumber(dto.phoneNumber()),
                user
        );
    }

}

