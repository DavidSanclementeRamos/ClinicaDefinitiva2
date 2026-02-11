package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.GuardianError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.*;
import java.time.LocalDateTime;
import java.util.List;

public class Guardian  {

    private final GuardianId guardianId;
    private Person person;
    private TypeGuardian typeGuardian;
    private final UserIdentityId userIdentityId;
    private final List<PatientId> patientList;
    private LocalDateTime lastUpdate;

    public Guardian(GuardianId guardianId, LocalDateTime lastUpdate, List<PatientId> patientList, Person person, TypeGuardian typeGuardian, UserIdentityId userIdentityId) {
        this.guardianId = guardianId;
        this.lastUpdate = lastUpdate;
        this.patientList = patientList;
        this.person = person;
        this.typeGuardian = typeGuardian;
        this.userIdentityId = userIdentityId;
    }


    public static Guardian registerGuardian(
            Person data,
            UserIdentityId userIdentityId,
            TypeGuardian typeGuardian) {


        if (data.getAge().isBetween(22, 60)) {
            throw new BusinessRuleViolationException(GuardianError.ERR_RESPONSIBLE_INVALID_AGE, EntityContext.GUARDIAN);
        }

        return new Guardian(
                null,
                LocalDateTime.now(),
                null,
                data,
                typeGuardian,
                userIdentityId);
    }

    public void updateContactData(Address address, PhoneNumber phoneNumber
                                   ) {

        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
         this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, TypeGuardian typeGuardian) {


        /** NOTA: Validación comentada intencionalmente.
         * Regla propuesta: impedir que paciente/guardián modifiquen datos sensibles
         * si tienen cita en las próximas 24h.
         * Decisión actual: NO implementar aún, porque puede ser demasiado rígido.
         * Solución futura: permitir cambios, pero registrar auditoría y notificar al personal.
         * Esto asegura trazabilidad sin bloquear modificaciones urgentes.

        if (this.schedule != null && this.schedule.hasAppointmentsWithinHour(24)) {
            throw new BusinessRuleViolationException(
                    ErrorCatalogXD.ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY,
                    EntityContext.PATIENT,
                    "No se puede modificar la fecha de nacimiento si el paciente tiene historial de citas"
            );
        }*/

       if (!this.person.getDateOfBirth().equals(dateOfBirth)) {

            if (!age.isBetween(22, 60)) {
                throw new BusinessRuleViolationException(GuardianError.ERR_RESPONSIBLE_INVALID_AGE, EntityContext.GUARDIAN);
            }
            Person data = new Person();
            this.person = data.updateSensitive(
                    age,
                    bloodType,
                    dateOfBirth,
                    dni,
                    documentoEPS,
                    fullname
            );

            this.typeGuardian = typeGuardian;
        }}


        public Outcome<Void> validateDeactivation() {
            if (patientList != null && !patientList.isEmpty()) {
                return Outcome.fail(new OutcomeDetail(
                        GuardianError.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS,
                        Severity.INFO,
                        Category.CLINICO,EntityContext.GUARDIAN
                ));
            }
            return Outcome.ok();
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

    public UserIdentityId getUserId() {
        return userIdentityId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
