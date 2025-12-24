package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
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
    private Schedule schedule;
    private UserId user;
    private List<Patient> patientList;
    private LocalDateTime lastUpdate;

    public Guardian(GuardianId guardianId, LocalDateTime lastUpdate, List<Patient> patientList, Person person, Schedule schedule, TypeGuardian typeGuardian, UserId user) {
        this.guardianId = guardianId;
        this.lastUpdate = lastUpdate;
        this.patientList = patientList;
        this.person = person;
        this.schedule = schedule;
        this.typeGuardian = typeGuardian;
        this.user = user;
    }

    public Guardian(GuardianId guardianId) {
        this.guardianId = guardianId;
    }

    // Un responsable puede ser registrado si:
    // Usuario activo.
    // Cumple con la edad requerida.
    // El responsable no sede el tope de pacientes a cargos.
    public static Guardian registerGuardian(
            Person data,
            UserIdentity user,
            TypeGuardian typeGuardian){

        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, EntityContext.GUARDIAN);

        if (data.getAge().isBetween(22,60)){
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_RESPONSIBLE_INVALID_AGE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.GUARDIAN);
        }

        return new Guardian(
                null,
                LocalDateTime.now(),
                null,
                data,
                null,
                typeGuardian,
                user.getId());
    }
   public void updateContactData(Person data,
                                 UserIdentity user) {

       UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.GUARDIAN);

       this.person= this.person.updateContact(data.getAddress(), data.getPhoneNumber());
       this.lastUpdate = LocalDateTime.now();
   }

    public void updateSensitiveData(Person data, UserIdentity user, TypeGuardian typeGuardian) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_USER_INACTIVE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.PATIENT);

        // RN-PATIENT-009: Validar cambio de fecha nacimiento
        if (!this.person.getDateOfBirth().equals(data.getDateOfBirth())) {
            if (this.schedule != null && this.schedule.hasAppointmentsWithinHour(24)) {
                throw new BusinessRuleViolationException(
                        ErrorCatalog.ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY,
                        com.example.ClinicaDefinitiva.domain.errors.EntityContext.PATIENT,
                        "No se puede modificar la fecha de nacimiento si el paciente tiene historial de citas"
                );
            }
        if (!data.getAge().isBetween(22, 60)) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_RESPONSIBLE_INVALID_AGE, com.example.ClinicaDefinitiva.domain.errors.EntityContext.GUARDIAN);
        }

        this.person = this.person.updateSensitive(
                data.getAge(),
                data.getBloodType(),
                data.getDateOfBirth(),
                data.getDni(),
                data.getDocumentoEPS(),
                data.getFullname()
        );

        this.typeGuardian = typeGuardian;
        this.lastUpdate = LocalDateTime.now();
    }

    @Override
    public Outcome assertCanBeDeactivated(String reason) {
        if(reason == null || reason.isBlank()){
            return Outcome.fail(new OutcomeDetail(ErrorCatalog.ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON, Severity.INFO, Category.ADMINISTRATIVO));
        }


        // Verificar pacientes asignados: no permitir desactivar si hay pacientes
        boolean hasAssignedPatients = (patientList != null && !patientList.isEmpty());
        if (hasAssignedPatients) {
            return Outcome.fail(new OutcomeDetail (ErrorCatalog.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS,Severity.INFO,Category.CLINICO));
        }
        return Outcome.ok();
    }

    @Override
    public UserId getUserId() {
        return user;
    }
}
