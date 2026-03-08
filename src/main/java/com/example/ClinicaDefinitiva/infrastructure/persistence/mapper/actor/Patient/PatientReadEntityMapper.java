package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.Patient;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;

import java.time.LocalDate;
import java.util.Objects;

public class PatientReadEntityMapper {

    public Patient toDomain(PatientEntity entity) {
        Objects.requireNonNull(entity, "PatientEntity must not be null");

        // Person: asumimos que siempre existe y está completo
         Address.of(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.getPostalCode()
        );

        DateOfBirth.of(LocalDate.parse(entity.getDateOfBirth()));
         Age.of(DateOfBirth.of(LocalDate.parse(entity.getDateOfBirth())));

        FullName.of(entity.getFirst(), entity.getLastName());
        PhoneNumber.of(entity.getPhoneNumber());
        BloodType.fromLabel(entity.getBloodType());
        Document.of(entity.getDni());



        // GuardianId y ContractId: asumimos que siempre existen
       // GuardianId guardianId = GuardianId.fromLong(entity.getGuardian().getGuardianId());
       // ContractId contractId = ContractId.fromString(entity.getContractId());

        return new Patient(
               null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}