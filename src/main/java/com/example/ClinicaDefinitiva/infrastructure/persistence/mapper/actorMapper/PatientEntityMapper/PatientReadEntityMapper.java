package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.PatientEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;

import java.time.LocalDate;
import java.util.Objects;

public class PatientReadEntityMapper {
    public Patient toDomain(PatientEntity entity) {
        Objects.requireNonNull(entity, "PatientEntity must not be null");

        // Person: asumimos que siempre existe y está completo
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

        // GuardianId y ContractId: asumimos que siempre existen
        GuardianId guardianId = GuardianId.fromLong(entity.getGuardian().getGuardianId());
        ContractId contractId = ContractId.fromString(entity.getContractId());

        return new Patient(
                PatientId.fromLong(entity.getPatientId()),
                person,
                guardianId,
                new UserId(entity.getUser()),
                entity.getLastUpdate(),
                contractId
        );
    }
}