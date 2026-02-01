package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.dentistEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.DentistEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// Mal escrito nombre de be ser de write ya que es de escritura
public class DentistReadEntityMapper {

// De Entity a Dominio

    public  Dentist toDomain(DentistEntity entity) {
        if (entity == null) return null;

        // specialties seguro contra null
        Set<Specialty> specialtiesSet = Optional.ofNullable(entity.getSpecialties())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(str -> !str.isEmpty())
                        .map(Specialty::new)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        return new Dentist(
                new DentistId(entity.getDentistId()),
                new Person(
                        new Address(entity.getStreet(), entity.getCity(), entity.getState(),
                                entity.getCountry(), entity.getPostalCode()),
                        new Age(new DateOfBirth(entity.getDateOfBirth())),
                        BloodType.fromLabel(entity.getBloodType()),
                        new DateOfBirth(entity.getDateOfBirth()),
                        new Document(entity.getDni()),
                        entity.getDocumentoEPS(),
                        new FullName(entity.getFirst(), entity.getLastName()),
                        new PhoneNumber(entity.getPhoneNumber())
                ),
                new Specialties(specialtiesSet),
                new UserId(entity.getUser()),
                new WorkingHours(entity.getStart(), entity.getEnd(),
                        entity.getDayOfWeek(), entity.getDeclaredHoursPerWeek()),
                DentistAvailabilityStatus.from(
                        DentistAvailabilityStatus.Status.valueOf(entity.getAvailabilityStatus())
                ),
                entity.getLastUpdate()

        );
    }
}
