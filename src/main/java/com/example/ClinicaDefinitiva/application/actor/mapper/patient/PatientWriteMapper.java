package com.example.ClinicaDefinitiva.application.actor.mapper.patient;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import java.util.Optional;
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
    return dto.guardianId() != null ? GuardianId.fromLong(dto.guardianId()) : null;
}

    // Address y PhoneNumber para contacto
public Optional<Address> toAddress(UpdatePatientContactDto dto) {
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

public Optional<PhoneNumber> toPhoneNumber(UpdatePatientContactDto dto) {
    return dto.phoneNumber().map(PhoneNumber::of);
}

// Campos sensibles
public Optional<BloodType> toBloodType(UpdatePatientSensitiveDto dto) {
    return dto.bloodType().map(BloodType::fromLabel);
}

public Optional<DateOfBirth> toDateOfBirth(UpdatePatientSensitiveDto dto) {
    return dto.dateOfBirth().map(DateOfBirth::of);
}

public Optional<Document> toDocument(UpdatePatientSensitiveDto dto) {
    return dto.dni().map(Document::of);
}

public Optional<String> toDocumentEPS(UpdatePatientSensitiveDto dto) {
    return dto.documentEPS();
}

public Optional<FullName> toFullName(UpdatePatientSensitiveDto dto) {
    if (dto.first().isEmpty() || dto.lastName().isEmpty()) {
        return Optional.empty();
    }
    return Optional.of(FullName.of(dto.first().get(), dto.lastName().get()));
}
    
}