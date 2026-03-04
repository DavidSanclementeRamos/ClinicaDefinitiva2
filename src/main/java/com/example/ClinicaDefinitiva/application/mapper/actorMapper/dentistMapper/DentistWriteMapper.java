package com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
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
        Set<Specialty> specialtiesSet = Optional.ofNullable(specialties)
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(str -> !str.isEmpty())
                        .map(Specialty::of)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
        return Specialties.of(specialtiesSet);
    }

    public WorkingHours toWorkingHours(WorkingHoursDto dto) {
        return WorkingHours.of(dto.start(), dto.end(), dto.dayOfWeek(), dto.declaredHoursPerWeek());
    }

    public UserIdentityId toUserIdentityId(CreateDentistDto dto) {
        return UserIdentityId.from(dto.user());
    }

    public LocalDateTime toLastUpdate(CreateDentistDto dto) {
        return dto.lastUpdate();
    }
}