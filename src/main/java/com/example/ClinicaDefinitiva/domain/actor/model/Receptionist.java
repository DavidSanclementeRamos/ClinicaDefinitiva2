package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import java.time.LocalDateTime;

public class Receptionist {

    private final ReceptionId id;
    private Person person;
    private Sector sector;
    private final String userId;
    private LocalDateTime lastUpdate;

    private Receptionist(ReceptionId id, Person person, Sector sector, String userId, LocalDateTime lastUpdate) {
        this.id = id;
        this.person = person;
        this.sector = sector;
        this.userId = userId;
        this.lastUpdate = lastUpdate;
    }

    public static Receptionist registerReceptionist(ReceptionId id, Person data, UserIdentity user, Sector sector) {
        LocalDateTime now = LocalDateTime.now();
        return new Receptionist(
                id,
                data,
                sector,
                user.getId(),
                now
        );
    }

    public void updateContactData(Address address, PhoneNumber phoneNumber, UserIdentity user) {
        this.person = person.updateContact(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dob, String dni,
                                     String documentoEPS, FullName fullname, Sector sector, UserIdentity user) {
        this.person = person.updateSensitive(age, bloodType, dob, dni,documentoEPS, fullname);
        this.sector = sector;
        this.lastUpdate = LocalDateTime.now();
    }

    // Getters (sin setters públicos para proteger invariantes)
    public ReceptionId getId() { return id; }
    public Person getPerson() { return person; }
    public Sector getSector() { return sector; }
    public String getUserId() { return userId; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
}