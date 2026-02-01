package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.guardianEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.GuardianEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuardianReadEntityMapper {
    // De Entity a Dominio
    public Guardian toDomain(GuardianEntity entity) {
        Objects.requireNonNull(entity, "GuardianEntity must not be null");

        Address address = new Address(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.getPostalCode()
        );

        DateOfBirth dob = new DateOfBirth(LocalDate.parse(entity.getDateOfBirth()));
        Age age = new Age(dob);

        FullName fullname = new FullName(entity.getFirst(), entity.getLastName());
        PhoneNumber phone = new PhoneNumber(entity.getPhoneNumber());
        BloodType bloodType = BloodType.fromLabel(entity.getBloodType());
        Document document = new Document(entity.getDni());

        Person person = new Person(
                address,
                age,
                bloodType,
                dob,
                document,
                entity.getDocumentEPS(),
                fullname,
                phone
        );

        List<PatientId> patientIds = entity.getPatientList().stream()
                .map(p -> PatientId.fromLong(p.getPatientId()))
                .collect(Collectors.toList());

        return new Guardian(
                GuardianId.fromLong(entity.getGuardianId()),
                entity.getLastUpdate(),
                patientIds,
                person,
                entity.getTypeGuardian(),
                new UserId(entity.getUser())
        );
    }
   }