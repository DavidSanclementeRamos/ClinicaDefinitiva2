package com.example.ClinicaDefinitiva.application.actor.mapper.dentist;

import com.example.ClinicaDefinitiva.application.actor.dto.dentist.CreateDentistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.WorkingHoursDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DentistWriteMapper {

    public Person toPerson(CreateDentistDto dto) {
        return Person.of(
                Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                Age.of(DateOfBirth.of(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                DateOfBirth.of(dto.dateOfBirth()),
                Document.of(dto.dni()),
                dto.documentoEPS(),
                FullName.of(dto.first(), dto.lastName()),
                PhoneNumber.of(dto.phoneNumber())
        );
    }

    public Specialties toSpecialties(String specialties) {
        if (specialties == null || specialties.isBlank()) {
            throw new IllegalArgumentException("Specialties cannot be empty");
        }
        Set<Specialty> specialtySet = Arrays.stream(specialties.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Specialty::fromString)
                .collect(Collectors.toSet());
        return Specialties.of(specialtySet);
    }

    public WorkingHours toWorkingHours(WorkingHoursDto dto) {
        return WorkingHours.of(dto.start(), dto.end(), dto.dayOfWeek(), dto.declaredHoursPerWeek());
    }

    public UserIdentityId toUserIdentityId(CreateDentistDto dto) {
        return UserIdentityId.from(dto.user());
    }

    public Optional<Address> toAddress(UpdateDentistContactDto dto) {
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

    public Optional<PhoneNumber> toPhoneNumber(UpdateDentistContactDto dto) {
        return dto.phoneNumber().map(PhoneNumber::of);
    }

    public Optional<BloodType> toBloodType(UpdateDentistSensitiveDto dto) {
        return dto.bloodType().map(BloodType::fromLabel);
    }

    public Optional<DateOfBirth> toDateOfBirth(UpdateDentistSensitiveDto dto) {
        return dto.dateOfBirth().map(DateOfBirth::of);
    }

    public Optional<Document> toDocument(UpdateDentistSensitiveDto dto) {
        return dto.dni().map(Document::of);
    }

    public Optional<String> toDocumentEPS(UpdateDentistSensitiveDto dto) {
        return dto.documentEPS();
    }

    public Optional<FullName> toFullName(UpdateDentistSensitiveDto dto) {
        if (dto.first().isEmpty() || dto.lastName().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(FullName.of(dto.first().get(), dto.lastName().get()));
    }

    public Optional<Specialties> toSpecialties(UpdateDentistSensitiveDto dto) {
        return dto.specialties().map(specialtiesStr -> {
            Set<Specialty> specialtySet = Arrays.stream(specialtiesStr.split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .map(Specialty::fromString)
                    .collect(Collectors.toSet());
            return Specialties.of(specialtySet);
        });
    }

    public Optional<WorkingHours> toWorkingHours(UpdateDentistSensitiveDto dto) {
        return dto.workingHoursDto().map(whDto ->
                WorkingHours.of(whDto.start(), whDto.end(), whDto.dayOfWeek(), whDto.declaredHoursPerWeek())
        );
    }
}