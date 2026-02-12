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

    public Receptionist fromCreateDto(CreateReceptionistDto dto) {
        return Receptionist.registerReceptionist(
                Person.of(
                        Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                        Age.of(DateOfBirth.of(dto.dateOfBirth())),
                        BloodType.fromLabel(dto.bloodType()),
                        DateOfBirth.of(dto.dateOfBirth()),
                        Document.of(dto.dni()),
                        dto.documentEPS(),
                        FullName.of(dto.first(), dto.lastName()),
                        PhoneNumber.of(dto.phoneNumber())
                ),
                UserIdentityId.from(dto.user()),
                Sector.of(dto.sector()),
                ShiftId.from(dto.shiftId())
        );
    }

    public void updateSensitiveFromDto(UpdateReceptionistSensitiveDto dto, Receptionist reception) {
        reception.updateSensitiveData(
                Age.of(DateOfBirth.of(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                DateOfBirth.of(dto.dateOfBirth()),
                Document.of(dto.dni()),
                dto.documentEPS(),
                FullName.of(dto.first(), dto.lastName()),
                Sector.of(dto.sector())
        );
    }

    public void updateContactFromDto(UpdateReceptionistContactDto dto, Receptionist reception) {
        reception.updateContactData(
                Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber())
        );
    }
}

