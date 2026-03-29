package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.Patient;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class PatientWriteEntityMapper {

    public PatientEntity toEntity(Patient domain) {
        PatientEntity entity = new PatientEntity();
        
        if (domain.getPatientId() != null) {
            entity.setId(domain.getPatientId().value());
        }
        
        entity.setPerson(mapToEmbeddable(domain.getPerson()));
        entity.setLastUpdate(domain.getLastUpdate());
        
        // Nota: userIdentity, guardian y contract se setean en el adapter
        
        return entity;
    }
    
    private PersonEmbeddable mapToEmbeddable(Person person) {
        if (person == null) return null;
        
        PersonEmbeddable embeddable = new PersonEmbeddable();
        
        embeddable.setFullName(person.getFullname().toString());
        embeddable.setDocumentType(person.getDni().toString());
        embeddable.setDocumentNumber(person.getDni().value());
        embeddable.setBloodType(person.getBloodType().getValue());
        embeddable.setBirthDate(person.getDateOfBirth().asDate());
        embeddable.setEpsDocument(person.getDocumentoEPS());
        embeddable.setPhoneNumber(person.getPhoneNumber().toString());
        embeddable.setAddress(person.getAddress().toString());
        
        return embeddable;
    }
}