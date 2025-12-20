package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
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
            throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_INVALID_AGE,ContextoEntidad.PATIENT);
        }

        if (!data.getAge().isAdult() && guardian == null) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN,ContextoEntidad.PATIENT);
        }

       // UserStatus.from(user.).mustBeActive(ErrorCatalog.ERR_PATIENT_INACTIVE, ContextoEntidad.PATIENT);

        return new Patient(id, data, guardian, user.getId(), null, null, lastUpdate, contractId);
    }

    // Actualizar datos de contacto
    public void updatePatientContact(Person data, UserIdentity user) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_PATIENT_INACTIVE, ContextoEntidad.PATIENT);
        this.person = this.person.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate = LocalDateTime.now();
    }

    // Actualizar datos sensibles
    public void updateSensitiveData(Person data, UserIdentity user) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_PATIENT_INACTIVE, ContextoEntidad.PATIENT);

        if (this.schedule != null && this.schedule.hasAppointmentsWithin(2)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_PATIENT_ACTIVE_SERVICES,ContextoEntidad.PATIENT);
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
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE, ContextoEntidad.PATIENT);

        if (shift == null) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_PATIENT_NO_SHIFT_ASSIGNED,ContextoEntidad.PATIENT);
        }
        if (!shift.isAvailableBetween(start, end)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_PATIENT_SHIFT_NOT_AVAILABLE,ContextoEntidad.PATIENT);
        }
    }

    /** Validar reagendamiento
    public void validateReschedule(LocalDateTime newStart, LocalDateTime newEnd) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE, ContextoEntidad.PATIENT);

        if (shift == null || !shift.isAvailableBetween(newStart, newEnd)) {
            throw new ShiftNotAvailableException(ContextoEntidad.PATIENT, "Nueva fecha fuera del turno asignado");
        }
    }*/

    // Validar responsable
    private void validarResponsable() {
        if (requiereResponsable() && !tieneResponsable()) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN,ContextoEntidad.PATIENT);
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
            return Outcome.fail(new OutcomeDetail(ErrorCatalog.EER_RECEPTIONIST_INACTIVATION_REQUIRES_REASON, Severity.INFO, Category.ADMINISTRATIVO));
        }

        if (schedule != null && schedule.hasAppointmentsWithin(DAYS_TO_BLOCK_DEACTIVATION)) {
            return Outcome.fail(new OutcomeDetail(ErrorCatalog.ERR_PATIENT_ACTIVE_SERVICES,Severity.INFO, Category.CLINICO));
        }

        return Outcome.ok();
    }

    @Override
    public UserId getUserId() {
        return user;
    }


}