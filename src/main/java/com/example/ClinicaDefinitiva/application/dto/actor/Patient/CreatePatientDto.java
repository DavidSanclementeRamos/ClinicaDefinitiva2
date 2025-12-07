package com.example.ClinicaDefinitiva.application.dto.actor.Patient;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.Shift;

import java.time.LocalDateTime;

public class CreatePatientDto {
    private PatientId patientId;
    private Person person;
    private GuardianId guardianId;
    private String user;
    private Shift shift;
    private Schedule schedule;
    private LocalDateTime lastUpdate;
    private ContractId contractId;

    public CreatePatientDto(ContractId contractId, GuardianId guardianId, LocalDateTime lastUpdate, PatientId patientId, Person person, Schedule schedule, Shift shift, String user) {
        this.contractId = contractId;
        this.guardianId = guardianId;
        this.lastUpdate = lastUpdate;
        this.patientId = patientId;
        this.person = person;
        this.schedule = schedule;
        this.shift = shift;
        this.user = user;
    }

    public CreatePatientDto() {
    }

    public ContractId getContractId() {
        return contractId;
    }

    public void setContractId(ContractId contractId) {
        this.contractId = contractId;
    }

    public GuardianId getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(GuardianId guardianId) {
        this.guardianId = guardianId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientId patientId) {
        this.patientId = patientId;
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

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
