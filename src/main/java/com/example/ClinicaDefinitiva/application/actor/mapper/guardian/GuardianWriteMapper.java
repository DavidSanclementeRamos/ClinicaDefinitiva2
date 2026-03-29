package com.example.ClinicaDefinitiva.application.actor.mapper.guardian;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class GuardianWriteMapper {

    public Person toPerson(CreateGuardianDto dto) {
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

    public UserIdentityId toUserIdentityId(CreateGuardianDto dto) {
        return UserIdentityId.from(dto.userId());
    }

    public TypeGuardian toTypeGuardian(CreateGuardianDto dto) {
        return TypeGuardian.of(dto.code(), dto.description());
    }

    public Age toAge(UpdateGuardianSensitiveDto dto) {
        return Age.of(DateOfBirth.of(dto.dateOfBirth()));
    }

    public BloodType toBloodType(UpdateGuardianSensitiveDto dto) {
        return BloodType.fromLabel(dto.bloodType());
    }

    public DateOfBirth toDateOfBirth(UpdateGuardianSensitiveDto dto) {
        return DateOfBirth.of(dto.dateOfBirth());
    }

    public Document toDocument(UpdateGuardianSensitiveDto dto) {
        return Document.of(dto.dni());
    }

    public String toDocumentEPS(UpdateGuardianSensitiveDto dto) {
        return dto.documentEPS();
    }

    public FullName toFullName(UpdateGuardianSensitiveDto dto) {
        return FullName.of(dto.first(), dto.lastName());
    }

    public TypeGuardian toTypeGuardian(UpdateGuardianSensitiveDto dto) {
        return TypeGuardian.of(dto.code(), dto.description());
    }

    public Address toAddress(UpdateGuardianContactDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(UpdateGuardianContactDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }
}