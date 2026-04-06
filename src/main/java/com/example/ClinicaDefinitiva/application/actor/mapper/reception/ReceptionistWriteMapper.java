package com.example.ClinicaDefinitiva.application.actor.mapper.reception;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import java.util.Optional;
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

   // Address y PhoneNumber para contacto
public Optional<Address> toAddress(UpdateReceptionistContactDto dto) {
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

public Optional<PhoneNumber> toPhoneNumber(UpdateReceptionistContactDto dto) {
    return dto.phoneNumber().map(PhoneNumber::of);
}

// Campos sensibles
public Optional<BloodType> toBloodType(UpdateReceptionistSensitiveDto dto) {
    return dto.bloodType().map(BloodType::fromLabel);
}

public Optional<DateOfBirth> toDateOfBirth(UpdateReceptionistSensitiveDto dto) {
    return dto.dateOfBirth().map(DateOfBirth::of);
}

public Optional<Document> toDocument(UpdateReceptionistSensitiveDto dto) {
    return dto.dni().map(Document::of);
}

public Optional<String> toDocumentEPS(UpdateReceptionistSensitiveDto dto) {
    return dto.documentEPS();
}

public Optional<FullName> toFullName(UpdateReceptionistSensitiveDto dto) {
    if (dto.first().isEmpty() || dto.lastName().isEmpty()) {
        return Optional.empty();
    }
    return Optional.of(FullName.of(dto.first().get(), dto.lastName().get()));
}

public Optional<Sector> toSector(UpdateReceptionistSensitiveDto dto) {
    return dto.sector().map(Sector::fromString);
}
}