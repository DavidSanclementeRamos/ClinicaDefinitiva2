package com.example.ClinicaDefinitiva.application.dto.guardian;

import com.example.ClinicaDefinitiva.application.dto.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.TypeGuardian;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public class ReadGuardian {
    private String guardianId;
    private Person person;
    private TypeGuardian typeGuardian;
    private Schedule schedule;
    private String userId;
    private List<ReadPatientDto> patientList;
    private LocalDateTime lastUpdate;

    public ReadGuardian(String userId, TypeGuardian typeGuardian, Schedule schedule, Person person, List<ReadPatientDto> patientList, LocalDateTime lastUpdate, String guardianId) {
        this.userId = userId;
        this.typeGuardian = typeGuardian;
        this.schedule = schedule;
        this.person = person;
        this.patientList = patientList;
        this.lastUpdate = lastUpdate;
        this.guardianId = guardianId;
    }

    public ReadGuardian() {
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

    public List<ReadPatientDto> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<ReadPatientDto> patientList) {
        this.patientList = patientList;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
