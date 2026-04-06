package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.guardian;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.GuardianEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class GuardianWriteEntityMapper {

    public GuardianEntity toEntity(Guardian domain) {
        GuardianEntity entity = new GuardianEntity();
        
        if (domain.getGuardianId() != null) {
            entity.setId(domain.getGuardianId().value());
        }
        
        entity.setPerson(mapToEmbeddable(domain.getPerson()));
        
        if (domain.getTypeGuardian() != null) {
            entity.setGuardianType(domain.getTypeGuardian().getCode());           }
        
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