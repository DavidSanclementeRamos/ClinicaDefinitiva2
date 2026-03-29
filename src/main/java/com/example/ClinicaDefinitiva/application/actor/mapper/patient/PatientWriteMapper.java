package com.example.ClinicaDefinitiva.application.actor.mapper.patient;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteMapper {

    public Person toPerson(CreatePatientDto dto) {
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

    public UserIdentityId toUserIdentityId(CreatePatientDto dto) {
        return UserIdentityId.from(dto.userId());
    }

    public GuardianId toGuardianId(CreatePatientDto dto) {
        return GuardianId.fromLong(dto.guardianId());
    }

    public Age toAge(UpdatePatientSensitiveDto dto) {
        return Age.of(DateOfBirth.of(dto.dateOfBirth()));
    }

    public BloodType toBloodType(UpdatePatientSensitiveDto dto) {
        return BloodType.fromLabel(dto.bloodType());
    }

    public DateOfBirth toDateOfBirth(UpdatePatientSensitiveDto dto) {
        return DateOfBirth.of(dto.dateOfBirth());
    }

    public Document toDocument(UpdatePatientSensitiveDto dto) {
        return Document.of(dto.dni());
    }

    public String toDocumentEPS(UpdatePatientSensitiveDto dto) {
        return dto.documentEPS();
    }

    public FullName toFullName(UpdatePatientSensitiveDto dto) {
        return FullName.of(dto.first(), dto.lastName());
    }

    public Address toAddress(UpdatePatientContactDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(UpdatePatientContactDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }
}