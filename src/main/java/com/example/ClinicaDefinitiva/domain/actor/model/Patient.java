package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import com.example.ClinicaDefinitiva.domain.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Patient  {

    private final PatientId patientId;
    private final UserId userId;
    private final GuardianId guardianId;
    private LocalDateTime lastUpdate;
    private ContractId contractId;
    private final List<TreatmentId> treatments;
    private Person person;


    public Patient(ContractId contractId, LocalDateTime lastUpdate, GuardianId guardianId, Person person, List<TreatmentId> treatments, UserId userId, PatientId patientId) {
        this.contractId = contractId;
        this.lastUpdate = lastUpdate;
        this.guardianId = guardianId;
        this.person = person;
        this.treatments = treatments;
        this.userId = userId;
        this.patientId = patientId;
    }

    public static Patient registerPatient(
                                          Person data,
                                          UserId userId,
                                          GuardianId guardianId,
                                          ContractId contractId) {

        if (!data.getAge().isEligibleForRegistration()) {
            throw new DomainAggregateException(PatientError.ERR_PATIENT_INVALID_AGE, EntityContext.PATIENT);
        }

        if (!data.getAge().isAdult() && guardianId == null) {
            throw new BusinessRuleViolationException(PatientError.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, EntityContext.PATIENT);
        }

        return new Patient(contractId,  LocalDate.now().atStartOfDay(), guardianId, data, null, userId, null);
    }

    public void updatePatientContact( Address address, PhoneNumber phoneNumber) {

        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname) {


        // LA VALIDACION DE SERVICIOS ACTIVOS EN LAS PÓCIMAS 2 HORAS DEBE IR EN UN DOMAIN SERVICE
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

        this.lastUpdate = LocalDateTime.now();
    }


    private void validateGuardian() {
        if (requiereResponsable() && !hasGuardian()) {
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
    public boolean hasGuardian() { return guardianId != null; }

    // Getters
    public PatientId getPatientId() { return patientId; }
    public Person getPerson() { return person; }
    public GuardianId getGuardianId() { return guardianId; }
    public UserId getUser() { return userId; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public ContractId getContractId() { return contractId; }

    public List<TreatmentId> getTreatments() {
        return treatments;
    }





}