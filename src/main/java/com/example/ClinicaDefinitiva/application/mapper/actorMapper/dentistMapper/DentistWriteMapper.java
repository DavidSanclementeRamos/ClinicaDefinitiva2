package com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DentistWriteMapper {

    public Dentist fromCreateDto(CreateDentistDto dto) {

        Set<Specialty> specialtiesSet = Optional.ofNullable(dto.specialties())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(str -> !str.isEmpty())
                        .map(Specialty::of) // uso de método estático
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        return Dentist.registerDentist(
                Person.of(
                        Address.of(dto.street(), dto.city(), dto.state(),
                                dto.country(), dto.postalCode()),
                        Age.of(DateOfBirth.of(dto.dateOfBirth())),
                        BloodType.fromLabel(dto.bloodType()),
                        DateOfBirth.of(dto.dateOfBirth()),
                        Document.of(dto.dni()),
                        dto.documentoEPS(),
                        FullName.of(dto.first(), dto.lastName()),
                        PhoneNumber.of(dto.phoneNumber())
                ),
                Specialties.of(specialtiesSet),
                UserIdentityId.from(dto.user()),
                WorkingHours.of(dto.start(), dto.end(),
                        dto.dayOfWeek(), dto.declaredHoursPerWeek()),
                dto.lastUpdate()
        );
    }

    public void updateSensitiveFromDto(UpdateDentistSensitiveDto dto, Dentist dentist) {
        Set<Specialty> specialtiesSet = Optional.ofNullable(dto.specialties())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(str -> !str.isEmpty())
                        .map(Specialty::of)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        dentist.updateSensitiveData(
                Age.of(DateOfBirth.of(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                DateOfBirth.of(dto.dateOfBirth()),
                Document.of(dto.dni()),
                dto.documentEPS(),
                FullName.of(dto.first(), dto.lastName()),
                Specialties.of(specialtiesSet),
                WorkingHours.of(dto.start(), dto.end(), dto.dayOfWeek(), dto.declaredHoursPerWeek())
        );
    }

    public void updateContactFromDto(UpdateDentistContactDto dto, Dentist dentist) {
        dentist.updateContactData(
                Address.of(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber())
        );
    }

}
