package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.dentist;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.actor.vo.WorkingHours;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DentistWriteEntityMapper {

    public DentistEntity toEntity(Dentist domain) {
        DentistEntity entity = new DentistEntity();
        
        // ID
        if (domain.getDentistId() != null) {
            entity.setId(domain.getDentistId().value());
        }
        
        // Person
        entity.setPerson(mapToEmbeddable(domain.getPersonData()));
        
String specialtiesStr = domain.getSpecialties().asSet().stream()
        .map(Specialty::getCode)
        .collect(Collectors.joining(","));
entity.setSpecialties(specialtiesStr);
        
        // WorkingHours - convertir a JSON
        if (domain.getWorkingHours() != null) {
            entity.setWorkHoursJson(workingHoursToJson(domain.getWorkingHours()));
        }
        
        // AvailabilityStatus
        if (domain.getAvailabilityStatus() != null) {
            entity.setAvailabilityStatus(domain.getAvailabilityStatus().getValue().name());
        }
        
        // Fechas
        entity.setLastUpdate(domain.getLastUpdate());
        entity.setVacationStart(domain.getVacationStart());
        entity.setVacationEnd(domain.getVacationEnd());
        entity.setDisabilityStart(domain.getIncapacityStart());
        entity.setDisabilityEnd(domain.getIncapacityEnd());
        entity.setDisabilityNote(domain.getIncapacityNote());
        
        // Nota: userIdentity y shift se setean en el adapter
        
        return entity;
    }
    
    private String workingHoursToJson(WorkingHours workingHours) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("start", workingHours.getStart().toString());
            node.put("end", workingHours.getEnd().toString());
            node.put("dayOfWeek", workingHours.getDayOfWeek().name());
            node.put("declaredHoursPerWeek", workingHours.getDeclaredHoursPerWeek());
            return mapper.writeValueAsString(node);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing working hours", e);
        }
    }
    
    private PersonEmbeddable mapToEmbeddable(Person person) {
        if (person == null) return null;
        
        PersonEmbeddable embeddable = new PersonEmbeddable();
        
        embeddable.setFullName(person.getFullname().toString());
        embeddable.setDocumentType(person.getDni().toString()); // Asumiendo que Document tiene tipo
        embeddable.setDocumentNumber(person.getDni().toString());
        embeddable.setBloodType(person.getBloodType().getValue());
        embeddable.setBirthDate(person.getDateOfBirth().asDate());
        embeddable.setEpsDocument(person.getDocumentoEPS());
        embeddable.setPhoneNumber(person.getPhoneNumber().Value());
        embeddable.setAddress(person.getAddress().toString());
        
        return embeddable;
    }
}