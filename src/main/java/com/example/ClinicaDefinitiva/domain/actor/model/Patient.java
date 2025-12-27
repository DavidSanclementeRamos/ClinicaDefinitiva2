package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.Shift;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.util.*;

import java.time.LocalDateTime;

public class Patient implements Actor {

    private final PatientId patientId;
    private final UserId user;
    private Person person;
    private GuardianId guardianId;
    private Shift shift;
    private Schedule schedule;
    private LocalDateTime lastUpdate;
    private ContractId contractId;

    private Patient(PatientId patientId,
                    Person person,
                    GuardianId guardianId,
                    UserId user,
                    Shift shift,
                    Schedule schedule,
                    LocalDateTime lastUpdate,
                    ContractId contractId) {
        this.patientId = patientId;
        this.person = person;
        this.guardianId = guardianId;
        this.user = user;
        this.shift = shift;
        this.schedule = schedule;
        this.lastUpdate = lastUpdate;
        this.contractId = contractId;
        validarResponsable(); // asegura consistencia al nacer
    }

    public static Patient registerPatient(PatientId id,
                                          Person data,
                                          UserIdentity user,
                                          GuardianId guardian,
                                          LocalDateTime lastUpdate,
                                          ContractId contractId) {

        if (!data.getAge().isEligibleForRegistration()) {
            throw new DomainAggregateException(ErrorCatalogXD.ERR_PATIENT_INVALID_AGE, EntityContext.PATIENT);
        }

        if (!data.getAge().isAdult() && guardian == null) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, EntityContext.PATIENT);
        }
// eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_PATIENT_INACTIVE, EntityContext.PATIENT);

        return new Patient(id, data, guardian, user.getId(), null, null, lastUpdate, contractId);
    }

    // Actualizar datos de contacto
    public void updatePatientContact(Person data, UserIdentity user) {
        // eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_PATIENT_INACTIVE, EntityContext.PATIENT);
        this.person = this.person.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate = LocalDateTime.now();
    }

    // Actualizar datos sensibles
    public void updateSensitiveData(Person data, UserIdentity user) {
        // eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_PATIENT_INACTIVE, EntityContext.PATIENT);

        if (this.schedule != null && this.schedule.hasAppointmentsWithin(2)) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_ACTIVE_SERVICES, EntityContext.PATIENT);
        }

        this.person = this.person.updateSensitive(
                data.getAge(),
                data.getBloodType(),
                data.getDateOfBirth(),
                data.getDni(),
                data.getDocumentoEPS(),
                data.getFullname()
        );
        this.lastUpdate = LocalDateTime.now();
    }



    // Validar si puede agendar cita
    public void canScheduleBetween(UserIdentity user,LocalDateTime start, LocalDateTime end) {
        // eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_RECEPTIONIST_NOT_EDITABLE, EntityContext.PATIENT);

        if (shift == null) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_NO_SHIFT_ASSIGNED, EntityContext.PATIENT);
        }
        if (!shift.isAvailableBetween(start, end)) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_SHIFT_NOT_AVAILABLE, EntityContext.PATIENT);
        }
    }

    /** Validar reagendamiento
    public void validateReschedule(LocalDateTime newStart, LocalDateTime newEnd) {
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_RECEPTIONIST_NOT_EDITABLE, EntityContext.PATIENT);

        if (shift == null || !shift.isAvailableBetween(newStart, newEnd)) {
            throw new ShiftNotAvailableException(EntityContext.PATIENT, "Nueva fecha fuera del turno asignado");
        }
    }**/

    // Validar responsable
    private void validarResponsable() {
        if (requiereResponsable() && !tieneResponsable()) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, EntityContext.PATIENT);
        }
    }

    public boolean requiereResponsable() { return !person.getAge().isAdult(); }
    public boolean tieneResponsable() { return guardianId != null; }

    // Getters
    public PatientId getPatientId() { return patientId; }
    public Person getPerson() { return person; }
    public GuardianId getGuardianId() { return guardianId; }
    public UserId getUser() { return user; }
    public Shift getShift() { return shift; }
    public Schedule getSchedule() { return schedule; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public ContractId getContractId() { return contractId; }

    @Override
    public Outcome assertCanBeDeactivated(String reason) {

        final int DAYS_TO_BLOCK_DEACTIVATION = 30;

        if(reason == null || reason.isBlank()){
            return Outcome.fail(new OutcomeDetail(ErrorCatalogXD.EER_RECEPTIONIST_INACTIVATION_REQUIRES_REASON, Severity.INFO, Category.ADMINISTRATIVO));
        }

        if (schedule != null && schedule.hasAppointmentsWithin(DAYS_TO_BLOCK_DEACTIVATION)) {
            return Outcome.fail(new OutcomeDetail(ErrorCatalogXD.ERR_PATIENT_ACTIVE_SERVICES,Severity.INFO, Category.CLINICO));
        }

        return Outcome.ok();
    }

    @Override
    public UserId getUserId() {
        return user;
    }


}