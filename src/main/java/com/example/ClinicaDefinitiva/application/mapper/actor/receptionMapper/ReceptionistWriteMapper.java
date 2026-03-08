package com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistWriteMapper {

    public Person toPerson(CreateReceptionistDto dto) {
        return Person.of(
            Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
            Age.of(DateOfBirth.of(dto.dateOfBirth())),
            BloodType.fromLabel(dto.bloodType()),
            DateOfBirth.of(dto.dateOfBirth()),
            Document.of(dto.dni()),
            dto.documentEPS(),
            FullName.of(dto.first(), dto.lastName()),
            PhoneNumber.of(dto.phoneNumber())
        );
    }

    public UserIdentityId toUserIdentityId(CreateReceptionistDto dto) {
        return UserIdentityId.from(dto.user());
    }

    public Sector toSector(CreateReceptionistDto dto) {
        return Sector.fromString(dto.sector());
    }

    public Age toAge(UpdateReceptionistSensitiveDto dto) {
        return Age.of(DateOfBirth.of(dto.dateOfBirth()));
    }

    public BloodType toBloodType(UpdateReceptionistSensitiveDto dto) {
        return BloodType.fromLabel(dto.bloodType());
    }

    public DateOfBirth toDateOfBirth(UpdateReceptionistSensitiveDto dto) {
        return DateOfBirth.of(dto.dateOfBirth());
    }

    public Document toDocument(UpdateReceptionistSensitiveDto dto) {
        return Document.of(dto.dni());
    }

    public String toDocumentEPS(UpdateReceptionistSensitiveDto dto) {
        return dto.documentEPS();
    }

    public FullName toFullName(UpdateReceptionistSensitiveDto dto) {
        return FullName.of(dto.first(), dto.lastName());
    }

    public Sector toSector(UpdateReceptionistSensitiveDto dto) {
        return Sector.fromString(dto.sector());
    }

    public Address toAddress(UpdateReceptionistContactDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(UpdateReceptionistContactDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }
}