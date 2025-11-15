package com.example.ClinicaDefinitiva.application.dto.receptionist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Sector;

import java.time.LocalDateTime;

public class ReadReceptionDto {
    private String receptionId;
    private Person person;
    private Sector sector;
    private String userId;
    private LocalDateTime lastUpdate;

    public ReadReceptionDto(LocalDateTime lastUpdate, Person person, String receptionId, Sector sector, String userId) {
        this.lastUpdate = lastUpdate;
        this.person = person;
        this.receptionId = receptionId;
        this.sector = sector;
        this.userId = userId;
    }

    public ReadReceptionDto() {
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
