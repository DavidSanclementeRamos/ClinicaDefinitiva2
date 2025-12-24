package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.util.*;

import java.time.LocalDateTime;
import java.util.List;

public class Dentist implements Actor {

    private final DentistId dentistId;
    private Person personData;
    private Specialties specialties;
    private DentistAvailabilityStatus availabilityStatus;
    private WorkingHours workingHours;
    private final UserId user;
    private final Schedule schedule;
    private LocalDateTime lastUpdate;

    private Dentist(DentistId dentistId,
                    Person personData,
                    Specialties specialties,
                    UserId user,
                    WorkingHours workingHours,
                    Schedule schedule,
                    DentistAvailabilityStatus availabilityStatus,
                    LocalDateTime lastUpdate) {
        this.dentistId = dentistId;
        this.personData = personData;
        this.specialties = specialties;
        this.user = user;
        this.workingHours = workingHours;
        this.schedule = schedule;
        this.availabilityStatus = availabilityStatus != null ? availabilityStatus: DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE);
        this.lastUpdate = lastUpdate;
    }

    // Fábrica semántica
    public static Dentist registerDentist(DentistId id,
                                          Person data,
                                          Specialties specialties,
                                          UserIdentity user,
                                          WorkingHours workingHours,
                                          WeeklyAvailability weeklyAvailability,
                                          LocalDateTime lastUpdate) {

        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);

        if (!data.getAge().isBetween(25, 130)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_AGE_INSUFFICIENT, EntityContext.DENTIST);
        }

        if (!weeklyAvailability.HorasRegistradas(40)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_MISSING_AVAILABILITY, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }

        Schedule schedule = new Schedule(List.of(), weeklyAvailability);

        return new Dentist(id, data, specialties, user.getId(), workingHours,
                schedule, DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE), lastUpdate);
    }

    public void updateSensitiveData(Person data, UserIdentity user, Specialties specialties, WorkingHours workingHours) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        ensureEditable();

        if (!data.getAge().isBetween(25, 130)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_AGE_INSUFFICIENT, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }

        this.personData = data.updateSensitive(data.getAge(), data.getBloodType(),
                data.getDateOfBirth(), data.getDni(), data.getDocumentoEPS(), data.getFullname());
        this.specialties = specialties;
        this.workingHours = workingHours;
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateContactData(Person data, UserIdentity user) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        ensureEditable();
        this.personData = personData.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate = LocalDateTime.now();
    }

    /** public void deactivate(UserIdentity user, int hoursRange) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE, EntityContext.DENTIST);

        if (this.schedule.hasAppointmentsWithinHours(hoursRange)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_ACTIVE_APPOINTMENTS,EntityContext.DENTIST, "No puede desactivarse con citas pendientes.");
        }
        this.availabilityStatus = DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE);
    }*/


    public void canScheduleBetween(UserIdentity user,LocalDateTime start, LocalDateTime end) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);

        ensureEditable();
        if (!workingHours.isWithinRange(start, end)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_OUT_OF_WORKING_HOURS, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }
    }

    public void validateVacationRequest(UserIdentity user,LocalDateTime vacationStart, LocalDateTime vacationEnd) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);

        if (!TimeIntervalRules.isValid(vacationStart, vacationEnd)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_INVALID_VACATION_RANGE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }

        List<Appointment> conflicts = schedule.getAppointments().stream()
                .filter(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), vacationStart, vacationEnd))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_VACATION_CONFLICT, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }
    }

    public void validateReschedule(UserIdentity user,LocalDateTime start, LocalDateTime end) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);

        if (!canWorkBetween(start, end)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }
    }

    public boolean canWorkBetween(LocalDateTime start, LocalDateTime end) {
        return workingHours != null && workingHours.isWithinRange(start, end);
    }

    public boolean isCompliantWithDeclaredWorkingHours() {
        return workingHours.isCompliantWithWorkingHours(schedule.getWeeklyAvailability());
    }

    private void ensureEditable() {
        if (!availabilityStatus.isOperational()) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_DENTIST_NOT_AVAILABLE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.DENTIST);
        }
    }

    // Getters
    public DentistId getDentistId() { return dentistId; }
    public Person getPersonData() { return personData; }
    public Specialties getSpecialties() { return specialties; }
    public DentistAvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public WorkingHours getWorkingHours() { return workingHours; }
    public UserId getUser() { return user; }
    public Schedule getSchedule() { return schedule; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    @Override
    public Outcome assertCanBeDeactivated(String reason) {

        if(reason == null || reason.isBlank()){
            return Outcome.fail(new OutcomeDetail(ErrorCatalog.EER_RECEPTIONIST_INACTIVATION_REQUIRES_REASON, Severity.INFO, Category.ADMINISTRATIVO));
        }
        if (this.schedule.hasAppointmentsWithinHours(24)) {
            return Outcome.fail(new OutcomeDetail(ErrorCatalog.ERR_DENTIST_ACTIVE_APPOINTMENTS,Severity.INFO, Category.CLINICO));
        }
        return Outcome.ok();
    }

    @Override
    public UserId getUserId() {
        return user;
    }
}
