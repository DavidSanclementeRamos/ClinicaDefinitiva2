package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.Treatment;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.GuardianError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.util.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Patient  {

    private final PatientId patientId;
    private final UserId user;
    private final List<TreatmentId> treatments;
    private Person person;
    private final GuardianId guardianId;
    private LocalDateTime lastUpdate;
    private ContractId contractId;

    public Patient(ContractId contractId, LocalDateTime lastUpdate, GuardianId guardianId, Person person, List<TreatmentId> treatments, UserId user, PatientId patientId) {
        this.contractId = contractId;
        this.lastUpdate = lastUpdate;
        this.guardianId = guardianId;
        this.person = person;
        this.treatments = treatments;
        this.user = user;
        this.patientId = patientId;
    }

    public static Patient registerPatient(
                                          Person data,
                                          UserIdentity user,
                                          GuardianId guardian) {

        if (!data.getAge().isEligibleForRegistration()) {
            throw new DomainAggregateException(PatientError.ERR_PATIENT_INVALID_AGE, EntityContext.PATIENT);
        }

        if (!data.getAge().isAdult() && guardian == null) {
            throw new BusinessRuleViolationException(PatientError.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, EntityContext.PATIENT);
        }


        return new Patient(null, data, guardian, user.getId(), LocalDate.now().atStartOfDay(), null);
    }

    // Actualizar datos de contacto
    public void updatePatientContact( Address address, PhoneNumber phoneNumber, UserIdentity user) {
        // eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados

        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    // Actualizar datos sensibles
    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname,UserIdentity user) {
        // eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados

        /** if (this.schedule != null && this.schedule.hasAppointmentsWithin(2)) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_ACTIVE_SERVICES, EntityContext.PATIENT);
        }**/
        Person data = new Person();
        this.person = data.updateSensitive(
                age,
                bloodType,
                dateOfBirth,
                dni,
                documentoEPS,
                fullname

        );
        this.contractId = contractId;
        this.lastUpdate = LocalDateTime.now();
    }



    // Validar si puede agendar cita

    // MIGRAR A DOIMAN SERVICE
   /** public void canScheduleBetween(UserIdentity user,LocalDateTime start, LocalDateTime end) {
        // eliminar el catalogo de error, debe provenir de user
// se debe validar que tampoco este suspendido o otros estados
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_RECEPTIONIST_NOT_EDITABLE, EntityContext.PATIENT);

        if (shift == null) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_NO_SHIFT_ASSIGNED, EntityContext.PATIENT);
        }
        if (!shift.isAvailableBetween(start, end)) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_SHIFT_NOT_AVAILABLE, EntityContext.PATIENT);
        }
    }*/

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
            throw new BusinessRuleViolationException(PatientError.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, EntityContext.PATIENT);
        }
    }
    public Outcome<Void> validateDeactivation() {
        if (treatments != null && !treatments.isEmpty()) {
            return Outcome.fail(new OutcomeDetail(
                    PatientError.ERR_PATIENT_ACTIVE_TREATMENT,
                    Severity.INFO,
                    Category.CLINICO,
                    EntityContext.PATIENT
            ));
        }
        return Outcome.ok();
    }

    public boolean requiereResponsable() { return !person.getAge().isAdult(); }
    public boolean tieneResponsable() { return guardianId != null; }

    // Getters
    public PatientId getPatientId() { return patientId; }
    public Person getPerson() { return person; }
    public GuardianId getGuardianId() { return guardianId; }
    public UserId getUser() { return user; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public ContractId getContractId() { return contractId; }

    public List<TreatmentId> getTreatments() {
        return treatments;
    }





}