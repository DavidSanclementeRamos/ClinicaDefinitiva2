package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.reception;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.ReceptionistEntity;
import org.springframework.stereotype.Component;

@Component
public class ReceptionWriteEntityMapper {

    public ReceptionistEntity toEntity(Receptionist domain) {
        ReceptionistEntity entity = new ReceptionistEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        
        entity.setPerson(mapToEmbeddable(domain.getPerson()));
        
        if (domain.getSector() != null) {
            entity.setSector(domain.getSector().getValue().name());
        }
        
        entity.setLastUpdate(domain.getLastUpdate());
        
        return entity;
    }
    
    private PersonEmbeddable mapToEmbeddable(Person person) {
        if (person == null) return null;
        
        PersonEmbeddable embeddable = new PersonEmbeddable();
        
        embeddable.setFullName(person.getFullname().toString());
        embeddable.setDocumentType(person.getDni().value());
        embeddable.setDocumentNumber(person.getDni().value());
        embeddable.setBloodType(person.getBloodType().getValue());
        embeddable.setBirthDate(person.getDateOfBirth().asDate());
        embeddable.setEpsDocument(person.getDocumentoEPS());
        embeddable.setPhoneNumber(person.getPhoneNumber().Value());
        embeddable.setAddress(person.getAddress().toString());
        
        return embeddable;
    }
}