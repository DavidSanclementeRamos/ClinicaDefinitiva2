package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.receptionEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ReceptionReadEntityMapper {

    public Receptionist toDomain(ReceptionistEntity entity) {
        if (entity == null) return null;

        Address address =  Address.of(
            entity.getStreet(),
            entity.getCity(),
            entity.getState(),
            entity.getCountry(),
            entity.getPostalCode()
        );

        DateOfBirth dateOfBirth = DateOfBirth.of(entity.getDateOfBirth());
        
        Age age = Age.of(dateOfBirth);

        PhoneNumber phoneNumber =  PhoneNumber.of(entity.getPhoneNumber());

        BloodType bloodType = BloodType.fromLabel(entity.getBloodType());

        Document document =  Document.of(entity.getDni());

        FullName fullName =  FullName.of(entity.getFirst(), entity.getLastName());

        Person person =  Person.of(address, age, bloodType, dateOfBirth, document, entity.getDocumentEPS(), fullName, phoneNumber);


        // Construir Sector
        Sector sector =  Sector.fromString(entity.getSector());

        // Construir ReceptionId
        ReceptionId receptionId = ReceptionId.of(entity.getReceptionistId());

        // Construir UserIdentityId
        UserIdentityId userIdentityId = UserIdentityId.from(entity.getUser());

        // Reconstruir Receptionist usando el nuevo método reconstruct
        return Receptionist.reconstruct(
            receptionId,
            person,
            sector,
            userIdentityId,
            entity.getLastUpdate()
        );
    }
}