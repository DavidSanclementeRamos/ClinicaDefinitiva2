package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.PatientEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;

import java.time.LocalDate;
import java.util.Objects;

public class PatientWriteEntityMapper {




    public PatientEntity toEntity(Patient domain) {
        Objects.requireNonNull(domain, "Patient domain object must not be null");

        PatientEntity entity = new PatientEntity();

        // Identificadores: asumimos que siempre existen
        entity.setPatientId(domain.getPatientId().getValue());
        entity.setGuardian(null); // referencia nula, adapter la seteará si es necesario
        entity.setContractId(String.valueOf(domain.getContractId().asLong()));

        // Person: asumimos que siempre existe y está completo
        Person person = domain.getPerson();

        entity.setDni(person.getDni().toString());
        entity.setFirst(person.getFullname().FirstName());
        entity.setLastName(person.getFullname().LastName());
        entity.setPhoneNumber(person.getPhoneNumber().toString());

        entity.setStreet(person.getAddress().Street());
        entity.setCity(person.getAddress().City());
        entity.setState(person.getAddress().State());
        entity.setCountry(person.getAddress().Country());
        entity.setPostalCode(person.getAddress().PostalCode());

        entity.setDateOfBirth(person.getDateOfBirth().asDate().toString());
        entity.setBloodType(person.getBloodType().getValue());
        entity.setDocumentEPS(person.getDocumentoEPS());

        return entity;
    }
}