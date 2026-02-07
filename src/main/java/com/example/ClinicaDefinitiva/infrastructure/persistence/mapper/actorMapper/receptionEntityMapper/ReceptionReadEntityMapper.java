package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.receptionEntityMapper;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;

import java.time.LocalDate;
import java.util.Objects;

public class ReceptionReadEntityMapper {

    public Receptionist toDomain(ReceptionistEntity entity) {
        Objects.requireNonNull(entity, "ReceptionistEntity must not be null");

        // Person: asumimos que siempre existe y está completo
        Address address = new Address(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.getPostalCode()
        );

        DateOfBirth dob = new DateOfBirth(LocalDate.parse(entity.getDateOfBirth()));
        Age age = new Age(dob);

        // Corregimos orden: primero nombre, luego apellido
        FullName fullname = new FullName(entity.getFirst(), entity.getLastName());
        PhoneNumber phone = new PhoneNumber(entity.getPhoneNumber());
        BloodType bloodType = BloodType.fromLabel(entity.getBloodType());
        Document document = new Document(entity.getDni());

        Person person = new Person(
                address,
                age,
                bloodType,
                dob,
                document,
                entity.getDocumentEPS(),
                fullname,
                phone
        );

        // Sector y ReceptionId: asumimos que siempre existen
        ReceptionId receptionId = ReceptionId.fromLong(entity.getReceptionistId());
        Sector sector = new Sector(entity.getSector());

        return new Receptionist(
                receptionId,
                person,
                sector,
                new UserId(entity.getUser()),
                entity.getLastUpdate()
        );
    }
}