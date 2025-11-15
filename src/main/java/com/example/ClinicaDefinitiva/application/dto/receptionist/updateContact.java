package com.example.ClinicaDefinitiva.application.dto.receptionist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Sector;

public class updateContact {
    private String receptionId;
    private Person personData;
    private String userId;
    private Sector sector; // opcional para updates sensibles

    public updateContact(Person personData, String receptionId, Sector sector, String userId) {
        this.personData = personData;
        this.receptionId = receptionId;
        this.sector = sector;
        this.userId = userId;
    }
    public updateContact() {
    }

    public Person getPersonData() {
        return personData;
    }

    public void setPersonData(Person personData) {
        this.personData = personData;
    }

    public String getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(String receptionId) {
        this.receptionId = receptionId;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
