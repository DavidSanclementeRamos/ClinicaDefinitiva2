package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.util.*;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Patient  {

    private final PatientId patientId;
    private final UserIdentityId userIdentityId;
    private final GuardianId guardianId;
    private LocalDateTime lastUpdate;
    private  ContractId contractId;
    private final List<TreatmentId> treatments;
    private Person person;


    private Patient(ContractId contractId, LocalDateTime lastUpdate, GuardianId guardianId, Person person, List<TreatmentId> treatments, UserIdentityId userIdentityId, PatientId patientId) {
        this.contractId = contractId;
        this.lastUpdate = lastUpdate;
        this.guardianId = guardianId;
        this.person = person;
        this.treatments = treatments;
        this.userIdentityId = userIdentityId;
        this.patientId = patientId;
    }

    public static Patient registerPatient(
                                          Person data,
                                          UserIdentityId userIdentityId,
                                          GuardianId guardianId
                                          ) {

        if (!data.getAge().isEligibleForRegistration()) {
            throw new DomainAggregateException(PatientError.ERR_PATIENT_INVALID_AGE, EntityContext.PATIENT);
        }

        if (!data.getAge().isAdult() && guardianId == null) {
            throw new BusinessRuleViolationException(PatientError.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, EntityContext.PATIENT);
        }

        return new Patient(null,  LocalDate.now().atStartOfDay(), guardianId, data, null, userIdentityId, null);
    }

    public void updatePatientContact(Address address, PhoneNumber phoneNumber) {


        this.person = person.withContactData(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname) {


        /** NOTA: Validación comentada intencionalmente.
         * Regla propuesta: impedir que paciente/guardián modifiquen datos sensibles
         * si tienen cita en las próximas 24h.
         * Decisión actual: NO implementar aún, porque puede ser demasiado rígido.
         * Solución futura: permitir cambios, pero registrar auditoría y notificar al personal.
         * Esto asegura trazabilidad sin bloquear modificaciones urgentes.

         if (this.schedule != null && this.schedule.hasAppointmentsWithin(2)) {
         throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_PATIENT_ACTIVE_SERVICES, EntityContext.PATIENT);
         }**/



        this.person = person.withSensitiveData(
                age,
                bloodType,
                dateOfBirth,
                dni,
                documentoEPS,
                fullname

        );

        this.lastUpdate = LocalDateTime.now();
    }

    public void assignContract(ContractId contractId) {
        this.contractId = Objects.requireNonNull(contractId, "ContractId cannot be null");
    }

    public void removeContract() {
        this.contractId = null;
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
    public UserIdentityId getUser() { return userIdentityId; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public ContractId getContractId() { return contractId; }

    public List<TreatmentId> getTreatments() {
        return treatments;
    }








}