package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import java.time.LocalDateTime;
import java.util.List;

public class Dentist   {

    private final DentistId dentistId;
    private Person personData;
    private Specialties specialties;
    private DentistAvailabilityStatus availabilityStatus;
    private WorkingHours workingHours;
    private final  UserId userId;
    private final List<TreatmentId> tratments;
    private LocalDateTime lastUpdate;



    public Dentist(DentistId dentistId,
                   Person personData,
                   Specialties specialties,
                   UserId userId,
                   WorkingHours workingHours,
                   DentistAvailabilityStatus availabilityStatus,
                   LocalDateTime lastUpdate,
                   List<TreatmentId> tratments) {
        this.dentistId = dentistId;
        this.personData = personData;
        this.specialties = specialties;
        this.userId = userId;
        this.workingHours = workingHours;
        this.availabilityStatus = availabilityStatus != null ? availabilityStatus: DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE);
        this.lastUpdate = lastUpdate;
        this.tratments = tratments;
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

        // SE SUPONE QUE WORKING HOURS YA VIENE VALIDADO AL CREAR EL VALUE OBJECT
        /** if (!workingHours.HorasRegistradas(40)) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_DENTIST_MISSING_AVAILABILITY, EntityContext.DENTIST);
        }*/

        return new Dentist(null, data, specialties, userId, workingHours
                , DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE),
                lastUpdate, List.of());

    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, Specialties specialties, WorkingHours workingHours) {
        ensureEditable();

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
        ensureEditable();
        Person data = new Person();
        this.personData = data.updateContact(address,phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }


    public void changeAvailability(DentistAvailabilityStatus newStatus) {
        if (!this.availabilityStatus.canTransitionTo(newStatus.getCurrent())) {
            throw new BusinessRuleViolationException(
                    VoActorError.ERR_AVAILABILITY_STATUS_INVALID_TRANSITION,
                    EntityContext.DENTIST
            );
        }
        this.availabilityStatus = newStatus;
    }




    //LA VALIDACION DE PODER AGENDAR UNA CITA SEGUN VALIDACION PREVIA DE ESTE AGREGADO SEVE IR EN UN DOMAIN SERVICE
    /**public void canScheduleBetween(UserIdentity user,LocalDateTime start, LocalDateTime end) {

        ensureEditable();
        if (!workingHours.isWithinRange(start, end)) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_OUT_OF_WORKING_HOURS, EntityContext.DENTIST);
        }
    }*/

    // LO MISMO QUE LA ANTERIOR
    /**public void validateVacationRequest(UserIdentity user,LocalDateTime vacationStart, LocalDateTime vacationEnd, Schedule schedule)  {

        if (!TimeIntervalRules.isValid(vacationStart, vacationEnd)) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_INVALID_VACATION_RANGE, EntityContext.DENTIST);
        }

        List<Appointment> conflicts = schedule.getAppointments().stream()
                .filter(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), vacationStart, vacationEnd))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_VACATION_CONFLICT, EntityContext.DENTIST);
        }
    }*/

    // LO MISMO QUE LA ANTERIOR
    /** public void validateReschedule(UserIdentity user,LocalDateTime start, LocalDateTime end) {

        if (!canWorkBetween(start, end)) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS, EntityContext.DENTIST);
        }
    }*/

    public boolean canWorkBetween(LocalDateTime start, LocalDateTime end) {
        return workingHours != null && workingHours.isWithinRange(start, end);
    }

   /** public boolean isCompliantWithDeclaredWorkingHours() {
        return workingHours.isCompliantWithWorkingHours(schedule.getWeeklyAvailability());
    }*/

    private void ensureEditable() {
        if (!availabilityStatus.isOperational()) {
            throw new BusinessRuleViolationException(DentistError.ERR_DENTIST_NOT_AVAILABLE, EntityContext.DENTIST);
        }
    }


    public List<TreatmentId> getTratments() {return tratments;}
    public DentistId getDentistId() { return dentistId; }
    public Person getPersonData() { return personData; }
    public Specialties getSpecialties() { return specialties; }
    public DentistAvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public WorkingHours getWorkingHours() { return workingHours; }
    public UserId getUserId() { return userId; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }



}
