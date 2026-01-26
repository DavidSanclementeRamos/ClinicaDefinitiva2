package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.receptionEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;

import java.util.Objects;

public class ReceptionReadEntityMapper {

    public ReceptionistEntity toEntity(Receptionist domain) {
        Objects.requireNonNull(domain, "Receptionist domain object must not be null");

        ReceptionistEntity entity = new ReceptionistEntity();

        // Identificadores y sector: asumimos que siempre existen
        entity.setReceptionistId(domain.getId().getValue());
        entity.setSector(domain.getSector().toString());

        // Person: asumimos que siempre existe y está completo
        Person person = domain.getPerson();

        entity.setDni(person.getDni().toString());
        entity.setFirst(person.getFullname().FirstName());
        entity.setLastName(person.getFullname().LastName()); // corregimos duplicación: antes se repetía setFirst

        entity.setPhoneNumber(person.getPhoneNumber().toString());

        entity.setStreet(person.getAddress().Street());
        entity.setCity(person.getAddress().City());
        entity.setState(person.getAddress().State());
        entity.setCountry(person.getAddress().Country());
        entity.setPostalCode(person.getAddress().PostalCode());

        entity.setDateOfBirth(person.getDateOfBirth().asDate().toString());
        entity.setBloodType(person.getBloodType().getValue());
        entity.setDocumentEPS(person.getDocumentoEPS());

        // User: asumimos que siempre existe
        entity.setUser(domain.getUser().toString());

        entity.setLastUpdate(domain.getLastUpdate());

        return entity;
    }
}