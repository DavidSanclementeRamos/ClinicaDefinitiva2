package com.example.ClinicaDefinitiva.application.dto.Patient;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.Shift;

import java.time.LocalDateTime;

public class UpdatePatientDto {
    private PatientId patientId;
    private Person person;
    private GuardianId guardianId;
    private String userId;
    private Shift shift;
    private Schedule schedule;
    private LocalDateTime lastUpdate;
    private ContractId contractId;

    public UpdatePatientDto(String userId, Shift shift, Schedule schedule, Person person, PatientId patientId, LocalDateTime lastUpdate, GuardianId guardianId, ContractId contractId) {
        this.userId = userId;
        this.shift = shift;
        this.schedule = schedule;
        this.person = person;
        this.patientId = patientId;
        this.lastUpdate = lastUpdate;
        this.guardianId = guardianId;
        this.contractId = contractId;
    }

    public UpdatePatientDto() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSensitiveUpdate() {
        return false;
    }
}
