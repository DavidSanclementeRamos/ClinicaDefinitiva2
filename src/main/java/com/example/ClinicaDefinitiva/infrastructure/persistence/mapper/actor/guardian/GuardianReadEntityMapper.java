package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.guardian;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.GuardianEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuardianReadEntityMapper {
    // De Entity a Dominio
    public Guardian toDomain(GuardianEntity entity) {
        Objects.requireNonNull(entity, "GuardianEntity must not be null");

      /*  Address address = new Address(
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
                .map(p -> PatientId.of(p.getPatientId()))
                .collect(Collectors.toList());*/

        return new Guardian(
                GuardianId.fromLong(entity.getGuardianId()),
                entity.getLastUpdate(),
                null,
                null,
                entity.getTypeGuardian(),
                //new UserIdentityId(entity.getUser())
                null
        );
    }
   }