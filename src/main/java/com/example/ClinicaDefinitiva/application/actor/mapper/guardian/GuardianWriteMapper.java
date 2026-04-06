package com.example.ClinicaDefinitiva.application.actor.mapper.guardian;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import java.util.Optional;
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

    public Optional<BloodType> toBloodType(UpdateGuardianSensitiveDto dto) {
        return dto.bloodType().map(BloodType::fromLabel);
    }

    public Optional<DateOfBirth> toDateOfBirth(UpdateGuardianSensitiveDto dto) {
        return dto.dateOfBirth().map(DateOfBirth::of);
    }

    public Optional<Document> toDocument(UpdateGuardianSensitiveDto dto) {
        return dto.dni().map(Document::of);
    }

    public Optional<String> toDocumentEPS(UpdateGuardianSensitiveDto dto) {
        return dto.documentEPS();
    }

    public Optional<FullName> toFullName(UpdateGuardianSensitiveDto dto) {
        if (dto.first().isEmpty() || dto.lastName().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(FullName.of(dto.first().get(), dto.lastName().get()));
    }

    public Optional<TypeGuardian> toTypeGuardian(UpdateGuardianSensitiveDto dto) {
        if (dto.code().isEmpty() || dto.description().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(TypeGuardian.of(dto.code().get(), dto.description().get()));
    }

  
public Optional<Address> toAddress(UpdateGuardianContactDto dto) {
    if (dto.street().isEmpty() || dto.city().isEmpty() || dto.state().isEmpty() ||
        dto.country().isEmpty() || dto.postalCode().isEmpty()) {
        return Optional.empty();
    }
    return Optional.of(Address.of(
        dto.street().get(),
        dto.city().get(),
        dto.state().get(),
        dto.country().get(),
        dto.postalCode().get()
    ));
}

public Optional<PhoneNumber> toPhoneNumber(UpdateGuardianContactDto dto) {
    return dto.phoneNumber().map(PhoneNumber::of);
}
}