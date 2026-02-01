package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.guardianEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.GuardianEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuardianWriteEntityMapper {


    // De Dominio a Entity
    public GuardianEntity toEntity(Guardian domain) {
        Objects.requireNonNull(domain, "Guardian domain object must not be null");

        GuardianEntity entity = new GuardianEntity();

        entity.setGuardianId(domain.getGuardianId().getValue());

        entity.setTypeGuardian(domain.getTypeGuardian());
        entity.setUser(domain.getUser().getValue());
        entity.setLastUpdate(domain.getLastUpdate());

        var p = domain.getPerson();

        entity.setDni(p.getDni().toString());
        entity.setFirst(p.getFullname().FirstName());
        entity.setLastName(p.getFullname().LastName());
        entity.setPhoneNumber(p.getPhoneNumber().toString());

        entity.setStreet(p.getAddress().Street());
        entity.setCity(p.getAddress().City());
        entity.setState(p.getAddress().State());
        entity.setCountry(p.getAddress().Country());
        entity.setPostalCode(p.getAddress().PostalCode());

        entity.setDateOfBirth(p.getDateOfBirth().asDate().toString());
        entity.setBloodType(p.getBloodType().getValue());
        entity.setDocumentEPS(p.getDocumentoEPS());

        return entity;
    }

}
