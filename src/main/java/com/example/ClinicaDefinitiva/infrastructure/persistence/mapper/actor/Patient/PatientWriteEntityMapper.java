package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.Patient;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteEntityMapper {

    public PatientEntity toEntity(Patient domain) {
        PatientEntity entity = new PatientEntity();

        if (domain.getPatientId() != null) {
            entity.setPatientId(domain.getPatientId().value());
        }

        if (domain.getContractId() != null) {
            entity.setContractId(domain.getContractId().getValue().toString());
        }

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
        
        entity.setAge(String.valueOf(person.getAge().Value()));

        entity.setAddress(person.getAddress().toString());

        if (domain.getUser() != null) {
            entity.setUser(domain.getUser().value().toString());
        }

        entity.setLastUpdate(domain.getLastUpdate());


        return entity;
    }
}