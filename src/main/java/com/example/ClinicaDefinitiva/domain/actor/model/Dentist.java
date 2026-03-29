package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;

import java.time.LocalDateTime;
import java.util.List;

public class Dentist   {

    private final DentistId dentistId;
    private final ShiftId shiftId;
    private Person personData;
    private Specialties specialties;
    private DentistAvailabilityStatus availabilityStatus;
    private WorkingHours workingHours;
    private final UserIdentityId userIdentityId;
    private final List<TreatmentId> treatmentId;
    private LocalDateTime lastUpdate;
    private  LocalDateTime vacationStart;
    private LocalDateTime vacationEnd;
    private  LocalDateTime incapacityStart;
    private  LocalDateTime incapacityEnd;
    private  String incapacityNote;



    private Dentist(DentistId dentistId, ShiftId shiftId,
                   Person personData,
                   Specialties specialties,
                   UserIdentityId userIdentityId,
                   WorkingHours workingHours,
                   List<TreatmentId> treatmentId){
        this.dentistId = dentistId;
        this.shiftId = shiftId;
        this.personData = personData;
        this.specialties = specialties;
        this.userIdentityId = userIdentityId;
        this.workingHours = workingHours;
        this.availabilityStatus =  DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE);
        this.lastUpdate  = LocalDateTime.now();
        this.treatmentId = treatmentId;
    }

    public static Dentist registerDentist(
                                          Person data,
                                          Specialties specialties,
                                          UserIdentityId userIdentityId,
                                          WorkingHours workingHours
                                           ) {


        if (!data.getAge().isBetween(25, 130)) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_AGE_INSUFFICIENT, EntityContext.DENTIST);
        }

        return new Dentist(null, null, data, specialties, userIdentityId, workingHours,List.of());

    }

    public void updateSensitiveData( BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, Specialties specialties, WorkingHours workingHours) {

        

        this.personData = personData.withSensitiveData( bloodType,
                dateOfBirth,dni, documentoEPS, fullname);
        this.specialties = specialties;
        this.workingHours = workingHours;
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateContactData(Address address, PhoneNumber phoneNumber) {

        this.personData = personData.withContactData(address,phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }


    public void applyVacation(LocalDateTime start, LocalDateTime end) {
        this.availabilityStatus = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.VACATION);
        this.vacationStart = start;
        this.vacationEnd = end;
        this.lastUpdate = LocalDateTime.now();
    }

    public void applyIncapacity(LocalDateTime start, LocalDateTime end, String note) {
        this.availabilityStatus = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.SICK_LEAVE);
        this.incapacityStart = start;
        this.incapacityEnd = end;
        this.incapacityNote = note;
        this.lastUpdate = LocalDateTime.now();
    }

    public void returnToAvailable() {
        this.availabilityStatus = DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE);
        this.vacationStart = null;
        this.vacationEnd = null;
        this.incapacityStart = null;
        this.incapacityEnd = null;
        this.incapacityNote = null;
    }

    public List<TreatmentId> getTreatmentId() {return treatmentId;}
    public DentistId getDentistId() { return dentistId; }
    public Person getPersonData() { return personData; }
    public Specialties getSpecialties() { return specialties; }
    public DentistAvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public WorkingHours getWorkingHours() { return workingHours; }
    public UserIdentityId getUserId() { return userIdentityId; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    public ShiftId getShiftId() {
        return shiftId;
    }

    public UserIdentityId getUserIdentityId() {
        return userIdentityId;
    }

    public LocalDateTime getVacationStart() {
        return vacationStart;
    }

    public LocalDateTime getVacationEnd() {
        return vacationEnd;
    }

    public LocalDateTime getIncapacityStart() {
        return incapacityStart;
    }

    public LocalDateTime getIncapacityEnd() {
        return incapacityEnd;
    }


    

    public String getIncapacityNote() {
        return incapacityNote;
    }
    

    public static Dentist withId(
        DentistId dentistId,
        Person personData,
        Specialties specialties,
        UserIdentityId userId,
        WorkingHours workingHours,
        LocalDateTime lastUpdate) {
    return new Dentist(dentistId, null, personData, specialties, userId, workingHours, List.of());
}
   
    // persistencia BD
public static Dentist reconstruct(
        DentistId dentistId,
        ShiftId shiftId,
        Person personData,
        Specialties specialties,
        UserIdentityId userIdentityId,
        WorkingHours workingHours,
        LocalDateTime lastUpdate,
        List<TreatmentId> treatmentId,
        DentistAvailabilityStatus availabilityStatus,
        LocalDateTime vacationStart,
        LocalDateTime vacationEnd,
        LocalDateTime incapacityStart,
        LocalDateTime incapacityEnd,
        String incapacityNote) {
    
    Dentist dentist = new Dentist(dentistId, shiftId, personData, specialties, 
                                   userIdentityId, workingHours, treatmentId);
    
    dentist.availabilityStatus = availabilityStatus;
    dentist.vacationStart = vacationStart;
    dentist.vacationEnd = vacationEnd;
    dentist.incapacityStart = incapacityStart;
    dentist.incapacityEnd = incapacityEnd;
    dentist.incapacityNote = incapacityNote;
    
    return dentist;
}

}
