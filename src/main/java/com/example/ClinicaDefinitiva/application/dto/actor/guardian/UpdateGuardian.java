package com.example.ClinicaDefinitiva.application.dto.actor.guardian;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.TypeGuardian;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

import java.time.LocalDateTime;

public class UpdateGuardian {
    private String guardianId;
    //private Address address;
   // private PhoneNumberMapper phoneNumber;
    private String UserId;
    private Person data;
    private TypeGuardian typeGuardian;
    private Schedule schedule;
    private LocalDateTime lastUpdate;

    public UpdateGuardian(Person data, String guardianId, LocalDateTime lastUpdate, Schedule schedule, TypeGuardian typeGuardian, String userId) {
        this.data = data;
        this.guardianId = guardianId;
        this.lastUpdate = lastUpdate;
        this.schedule = schedule;
        this.typeGuardian = typeGuardian;
        UserId = userId;
    }

    public UpdateGuardian() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(String guardianId) {
        this.guardianId = guardianId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Person getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public TypeGuardian getTypeGuardian() {
        return typeGuardian;
    }

    public void setTypeGuardian(TypeGuardian typeGuardian) {
        this.typeGuardian = typeGuardian;
    }
}
