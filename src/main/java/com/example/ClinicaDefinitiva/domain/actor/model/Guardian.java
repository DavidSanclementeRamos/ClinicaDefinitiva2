package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.GuardianError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.util.*;

import java.time.LocalDateTime;
import java.util.List;

public class Guardian implements Actor {

    private final GuardianId guardianId;
    private Person person;
    private TypeGuardian typeGuardian;
    private UserId user;
    private List<PatientId> patientList;
    private LocalDateTime lastUpdate;

    public Guardian(GuardianId guardianId, LocalDateTime lastUpdate, List<PatientId> patientList, Person person, TypeGuardian typeGuardian, UserId user) {
        this.guardianId = guardianId;
        this.lastUpdate = lastUpdate;
        this.patientList = patientList;
        this.person = person;
        this.typeGuardian = typeGuardian;
        this.user = user;
    }


    public static Guardian registerGuardian(
            Person data,
            UserIdentity user,
            TypeGuardian typeGuardian) {

        UserStatus.from(user).mustBeActive(GuardianError.ERR_USER_INACTIVE, EntityContext.GUARDIAN);

        if (data.getAge().isBetween(22, 60)) {
            throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_RESPONSIBLE_INVALID_AGE, EntityContext.GUARDIAN);
        }

        return new Guardian(
                null,
                LocalDateTime.now(),
                null,
                data,
                typeGuardian,
                user.getId());
    }

    public void updateContactData(Address address, PhoneNumber phoneNumber,
                                  UserIdentity user) {

        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_USER_INACTIVE, EntityContext.GUARDIAN);
        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
        // this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, UserIdentity user, TypeGuardian typeGuardian) {
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_USER_INACTIVE, EntityContext.PATIENT);
        Schedule schedule = new Schedule();
        // RN-PATIENT-009: Validar cambio de fecha nacimiento

        // MIGRAR A DOIMAN SERVICE
       /** if (!this.person.getDateOfBirth().equals(dateOfBirth)) {
            if (this.schedule != null && this.schedule.hasAppointmentsWithinHour(24)) {
                throw new BusinessRuleViolationException(
                        ErrorCatalogXD.ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY,
                        EntityContext.PATIENT,
                        "No se puede modificar la fecha de nacimiento si el paciente tiene historial de citas"
                );
            }
            if (!age.isBetween(22, 60)) {
                throw new BusinessRuleViolationException(ErrorCatalogXD.ERR_RESPONSIBLE_INVALID_AGE, EntityContext.GUARDIAN);
            }*/
            Person data = new Person();
            this.person = data.updateSensitive(
                    age,
                    bloodType,
                    dateOfBirth,
                    dni,
                    documentoEPS,
                    fullname
            );

            //this.typeGuardian = t
            this.typeGuardian = typeGuardian;
        }

        @Override
        public Outcome<H> assertCanBeDeactivated (String reason){
            if (reason == null || reason.isBlank()) {
                return Outcome.fail(new OutcomeDetail(ErrorCatalogXD.ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON, Severity.INFO, Category.ADMINISTRATIVO));
            }


            // Verificar pacientes asignados: no permitir desactivar si hay pacientes
            boolean hasAssignedPatients = (patientList != null && !patientList.isEmpty());
            if (hasAssignedPatients) {
                return Outcome.fail(new OutcomeDetail(ErrorCatalogXD.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS, Severity.INFO, Category.CLINICO));
            }
            return Outcome.ok();
        }

        @Override
        public UserId getUserId () {
            return user;
        }



    public List<PatientId> getPatientList() {
        return patientList;
    }

    public TypeGuardian getTypeGuardian() {
        return typeGuardian;
    }

    public Person getPerson() {
        return person;
    }

    public GuardianId getGuardianId() {
        return guardianId;
    }

    public UserId getUser() {
        return user;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
