package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.Operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.time.LocalDateTime;

public class Receptionist   {

    private final ReceptionId id;
    private final ShiftId shiftId;
    private Person person;
    private Sector sector;
    private final UserIdentityId userIdentityId;
    private LocalDateTime lastUpdate;

    public Receptionist(LocalDateTime lastUpdate, UserIdentityId userIdentityId, Sector sector, Person person, ShiftId shiftId, ReceptionId id) {
        this.lastUpdate = lastUpdate;
        this.userIdentityId = userIdentityId;
        this.sector = sector;
        this.person = person;
        this.shiftId = shiftId;
        this.id = id;
    }

    public static Receptionist registerReceptionist(
            Person data,
            UserIdentityId userIdentityId,
            Sector sector,
            ShiftId shiftId) {
        return new Receptionist(LocalDateTime.now(), userIdentityId, sector, data,shiftId, null);
    }



    public void updateContactData(Address address, PhoneNumber phoneNumber) {
        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, Sector sector) {
        Person data = new Person();
        this.person = data.updateSensitive(
                age,
                bloodType,
                dateOfBirth,
                dni,
                documentoEPS,
                fullname);
        this.lastUpdate = LocalDateTime.now();
        this.sector = sector;
    }


    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
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