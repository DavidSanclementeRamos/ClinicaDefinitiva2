package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.receptionEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;
import org.springframework.stereotype.Component;

@Component
public class ReceptionWriteEntityMapper {

    public ReceptionistEntity toEntity(Receptionist domain) {

        ReceptionistEntity entity = new ReceptionistEntity();

            entity.setReceptionistId(domain.getId().getValue());
        

            entity.setSector(domain.getSector().getDescription());
       

        // Person
        Person person = domain.getPerson();
            // Documentos
                entity.setDni(person.getDni().value());
           
            
                entity.setFirst(person.getFullname().FirstName());
                entity.setLastName(person.getFullname().LastName());
            
            
            // Teléfono
                entity.setPhoneNumber(person.getPhoneNumber().Value());
            
            
            // Fecha de nacimiento
                entity.setDateOfBirth(person.getDateOfBirth().Value());
            
            
            // Tipo de sangre
                entity.setBloodType(person.getBloodType().getValue());
            
            
            entity.setDocumentEPS(person.getDocumentoEPS());
            
                entity.setAge(String.valueOf(person.getAge().Value()));
            
            
                entity.setStreet(person.getAddress().Street());
                entity.setCity(person.getAddress().City());
                entity.setState(person.getAddress().State());
                entity.setCountry(person.getAddress().Country());
                entity.setPostalCode(person.getAddress().PostalCode());
                entity.setAddress(person.getAddress().toString()); // Campo address completo
            
        

        // UserIdentityId
            entity.setUser(domain.getUserIdentityId().value());
        

        // LastUpdate
        entity.setLastUpdate(domain.getLastUpdate());

        return entity;
    }
}