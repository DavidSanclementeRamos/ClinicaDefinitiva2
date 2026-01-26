package com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DentistWriteMapper {

    // DTO de entrada → dominio (VOs/Agregado).
    public Dentist dtoCreateToDentist(CreateDentistDto dto  ) {

        // specialties seguro contra null
        Set<Specialty> specialtiesSet = Optional.ofNullable(dto.specialties())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(str -> !str.isEmpty())
                        .map(Specialty::new)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        UserIdentity user = new UserIdentity();
        return Dentist.registerDentist(
                new Person(
                        new Address(dto.street(), dto.city(), dto.state(),
                                dto.country(), dto.postalCode()),
                        new Age(new DateOfBirth(dto.dateOfBirth())),
                        BloodType.fromLabel(dto.bloodType()),
                        new DateOfBirth(dto.dateOfBirth()),
                        new Document(dto.dni()),
                        dto.documentoEPS(),
                        new FullName(dto.first(), dto.lastName()),
                        new PhoneNumber(dto.phoneNumber())
                ),
                new Specialties(specialtiesSet),
                user,
                new WorkingHours(dto.start(), dto.end(),
                        dto.dayOfWeek(), dto.declaredHoursPerWeek()),
                dto.lastUpdate()
        );

    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateSensitiveToDentist(UpdateDentistSensitiveDto dto, Dentist dentist) {
        Set<Specialty> specialtiesSet = Optional.ofNullable(dto.specialties())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(str -> !str.isEmpty())
                        .map(Specialty::new)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        dentist.updateSensitiveData(
                new Age(new DateOfBirth(dto.dateOfBirth())),
                BloodType.fromLabel(dto.bloodType()),
                new DateOfBirth(dto.dateOfBirth()),
                new Document(dto.dni()),
                dto.documentEPS(),
                new FullName(dto.first(), dto.lastName()),
                new UserIdentity(),
                new Specialties(specialtiesSet),
                new WorkingHours(dto.start(), dto.end(), dto.dayOfWeek(), dto.declaredHoursPerWeek())
        );
    }

    // DTO de entrada → dominio (VOs/Agregado).
    public void dtoUpdateContactToDentist(UpdateDentistContactDto dto, Dentist dentist) {
        UserIdentity user = new UserIdentity();

        dentist.updateContactData(
                new Address(dto.street(), dto.city(), dto.state(),
                        dto.country(), dto.postalCode()),

                new PhoneNumber(dto.phoneNumber()),
                user
        );
    }
    // DTO de entrada → dominio (VOs/Agregado).
    public DentistAvailabilityStatus toAvailabilityStatus(UpdateDentistStatusDto request) {
        return DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.valueOf(request.availabilityStatus())); }



}
