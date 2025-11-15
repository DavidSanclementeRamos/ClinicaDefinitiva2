package com.example.ClinicaDefinitiva.application.dto.receptionist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Sector;

public class CreateReceptionDto {
    private String receptionId; // opcional: si se genera en el servidor puede ser null
    private Person personData;
    private String userId;
    private Sector sector;

    public CreateReceptionDto(Person personData, String receptionId, Sector sector, String userId) {
        this.personData = personData;
        this.receptionId = receptionId;
        this.sector = sector;
        this.userId = userId;
    }

    public CreateReceptionDto() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(String receptionId) {
        this.receptionId = receptionId;
    }

    public Person getPersonData() {
        return personData;
    }

    public void setPersonData(Person personData) {
        this.personData = personData;
    }
}