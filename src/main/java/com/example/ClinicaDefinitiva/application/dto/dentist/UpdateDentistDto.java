package com.example.ClinicaDefinitiva.application.dto.dentist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.WorkingHours;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

import java.time.LocalDateTime;

public class UpdateDentistDto {
    private String id;
    private Person updatePersonContactData;
    private  LocalDateTime lastUpdate;
    private Specialties specialties;
    private WorkingHours workingHours;
    private String userId;
    private Schedule schedule;

    public UpdateDentistDto(String id, LocalDateTime lastUpdate, Schedule schedule, Specialties specialties, Person updatePersonContactData, String userId, WorkingHours workingHours) {
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.schedule = schedule;
        this.specialties = specialties;
        this.updatePersonContactData = updatePersonContactData;
        this.userId = userId;
        this.workingHours = workingHours;
    }

    public Specialties getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Specialties specialties) {
        this.specialties = specialties;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UpdateDentistDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Person getUpdatePersonContactData() {
        return updatePersonContactData;
    }

    public void setUpdatePersonContactData(Person updatePersonContactData) {
        this.updatePersonContactData = updatePersonContactData;
    }
}
