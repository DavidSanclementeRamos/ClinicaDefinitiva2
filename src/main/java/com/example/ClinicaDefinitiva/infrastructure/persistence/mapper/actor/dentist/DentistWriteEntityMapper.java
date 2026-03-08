package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.dentist;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.DentistEntity;
import org.springframework.stereotype.Component;

@Component
public class DentistWriteEntityMapper {


    // De Dominio a Entity
    public DentistEntity toEntity(Dentist domain) {
        // if (domain == null) return null;

        DentistEntity entity = new DentistEntity();
        entity.setDentistId(domain.getDentistId().value());
        entity.setDni(domain.getPersonData().getDni().toString());
        // FullName
        entity.setFirst(domain.getPersonData().getFullname().FirstName());
        entity.setLastName(domain.getPersonData().getFullname().LastName());
        // Phone
        entity.setPhoneNumber(domain.getPersonData().getPhoneNumber().toString());
        // Address
        entity.setStreet(domain.getPersonData().getAddress().Street());
        entity.setCity(domain.getPersonData().getAddress().City());
        entity.setState(domain.getPersonData().getAddress().State());
        entity.setCountry(domain.getPersonData().getAddress().Country());
        entity.setPostalCode(domain.getPersonData().getAddress().PostalCode());

        entity.setDateOfBirth(domain.getPersonData().getDateOfBirth().asDate()); // VO compatible
        entity.setBloodType(domain.getPersonData().getBloodType().getValue());
        entity.setDocumentoEPS(domain.getPersonData().getDocumentoEPS());
        entity.setSpecialties(domain.getSpecialties().toString());
        entity.setAvailabilityStatus(domain.getAvailabilityStatus().toString());

        // WorkingHours
        entity.setStart(domain.getWorkingHours().getStart());
        entity.setEnd(domain.getWorkingHours().getEnd());
        entity.setDayOfWeek(domain.getWorkingHours().getDayOfWeek());
        // entity.setDeclaredHoursPerWeek(domain.getWorkingHours().);
        entity.setUser(domain.getUserId().toString());
        entity.setLastUpdate(domain.getLastUpdate());

        return entity;
    }

}
