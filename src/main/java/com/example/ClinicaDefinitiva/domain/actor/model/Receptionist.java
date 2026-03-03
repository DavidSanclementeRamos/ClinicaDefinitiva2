package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;

import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.ReceptionistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;

import java.time.LocalDateTime;

public class Receptionist   {

    private final ReceptionId id;
    private  Person person;
    private Sector sector;
    private final UserIdentityId userIdentityId;
    private LocalDateTime lastUpdate;

    private Receptionist(LocalDateTime lastUpdate, UserIdentityId userIdentityId, Sector sector, Person person, ReceptionId id) {
        this.lastUpdate = lastUpdate;
        this.userIdentityId = userIdentityId;
        this.sector = sector;
        this.person = person;
        this.id = id;
    }

    public static Receptionist registerReceptionist(
            Person data,
            UserIdentityId userIdentityId,
            Sector sector
             ) {
        if (!data.getAge().isBetween(20, 130)) {
            throw new BusinessRuleViolationException(ReceptionistError.ERR_RECEPTIONIST_AGE_INSUFFICIENT, EntityContext.DENTIST);
        }
        return new Receptionist(LocalDateTime.now(), userIdentityId, sector, data, null);
    }



    public void updateContactData(Address address, PhoneNumber phoneNumber) {

        this.person = person.withContactData(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, Sector sector) {

        this.person = person.withSensitiveData(
                age,
                bloodType,
                dateOfBirth,
                dni,
                documentoEPS,
                fullname);
        this.lastUpdate = LocalDateTime.now();
        this.sector = sector;
    }



    public UserIdentityId getUserIdentityId() {
        return userIdentityId;
    }

    public ReceptionId getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Sector getSector() {
        return sector;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }


}