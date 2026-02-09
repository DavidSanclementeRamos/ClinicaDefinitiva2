package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class Dentist   {

    private final DentistId dentistId;
    private Person personData;
    private Specialties specialties;
    private DentistAvailabilityStatus availabilityStatus;
    private WorkingHours workingHours;
    private final  UserId userId;
    private final List<TreatmentId> treatmentId;
    private LocalDateTime lastUpdate;
    private  LocalDateTime vacationStart;
    private LocalDateTime vacationEnd;
    private  LocalDateTime incapacityStart;
    private  LocalDateTime incapacityEnd;
    private  String incapacityNote;
    private  Instant lastUpdated;



    public Dentist(DentistId dentistId,
                   Person personData,
                   Specialties specialties,
                   UserId userId,
                   WorkingHours workingHours,
                   LocalDateTime lastUpdate,
                   List<TreatmentId> treatmentId){
        this.dentistId = dentistId;
        this.personData = personData;
        this.specialties = specialties;
        this.userId = userId;
        this.workingHours = workingHours;
        this.availabilityStatus =  DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE);
        this.lastUpdate = lastUpdate;
        this.treatmentId = treatmentId;
    }

    public static Dentist registerDentist(
                                          Person data,
                                          Specialties specialties,
                                          UserId userId,
                                          WorkingHours workingHours,
                                          LocalDateTime lastUpdate
                                           ) {


        if (!data.getAge().isBetween(25, 130)) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_AGE_INSUFFICIENT, EntityContext.DENTIST);
        }

        return new Dentist(null, data, specialties, userId, workingHours,

                lastUpdate, List.of());

    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, Specialties specialties, WorkingHours workingHours) {

        if (!age.isBetween(25, 130)) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_AGE_INSUFFICIENT, EntityContext.DENTIST);
        }
        Person data = new Person();
        this.personData = data.updateSensitive(age, bloodType,
                dateOfBirth,dni, documentoEPS, fullname);
        this.specialties = specialties;
        this.workingHours = workingHours;
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateContactData(Address address, PhoneNumber phoneNumber) {
        Person data = new Person();
        this.personData = data.updateContact(address,phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }


    public void applyVacation(LocalDateTime start, LocalDateTime end) {
        this.availabilityStatus = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.VACATION);
        this.vacationStart = start;
        this.vacationEnd = end;
        this.lastUpdated = Instant.now();
    }

    public void applyIncapacity(LocalDateTime start, LocalDateTime end, String note) {
        this.availabilityStatus = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.SICK_LEAVE);
        this.incapacityStart = start;
        this.incapacityEnd = end;
        this.incapacityNote = note;
        this.lastUpdated = Instant.now();
    }

    public void returnToAvailable() {
        this.availabilityStatus = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE);
        this.vacationStart = null;
        this.vacationEnd = null;
        this.incapacityStart = null;
        this.incapacityEnd = null;
        this.incapacityNote = null;
        this.lastUpdated = Instant.now();
    }

    public List<TreatmentId> getTreatmentId() {return treatmentId;}
    public DentistId getDentistId() { return dentistId; }
    public Person getPersonData() { return personData; }
    public Specialties getSpecialties() { return specialties; }
    public DentistAvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public WorkingHours getWorkingHours() { return workingHours; }
    public UserId getUserId() { return userId; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }

}
