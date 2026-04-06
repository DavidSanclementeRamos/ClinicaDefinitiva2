package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.GuardianError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.*;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Guardian  {

    private final GuardianId guardianId;
    private Person person;
    private TypeGuardian typeGuardian;
    private final UserIdentityId userIdentityId;
    private final List<PatientId> patientList;
    private LocalDateTime lastUpdate;

    private Guardian(GuardianId guardianId, List<PatientId> patientList, Person person, TypeGuardian typeGuardian, UserIdentityId userIdentityId) {
        this.guardianId = guardianId;
        this.lastUpdate = lastUpdate = LocalDateTime.now();
        this.patientList = patientList;
        this.person = person;
        this.typeGuardian = typeGuardian;
        this.userIdentityId = userIdentityId;
    }


    public static Guardian registerGuardian(
            Person data,
            UserIdentityId userIdentityId,
            TypeGuardian typeGuardian) {


        if (!data.getAge().isBetween(22, 60)) {
            throw new BusinessRuleViolationException(GuardianError.ERR_RESPONSIBLE_INVALID_AGE, EntityContext.GUARDIAN);
        }

        return new Guardian(
                null,
                null,
                data,
                typeGuardian,
                userIdentityId);
    }

    public void updateContactData(Optional<Address> newAddress, Optional<PhoneNumber> newPhoneNumber) {
    Person currentPerson = this.person;
    
    Address finalAddress = newAddress.orElse(currentPerson.getAddress());
    PhoneNumber finalPhoneNumber = newPhoneNumber.orElse(currentPerson.getPhoneNumber());
    
    if (newAddress.isPresent() || newPhoneNumber.isPresent()) {
        this.person = currentPerson.withContactData(finalAddress, finalPhoneNumber);
    }
    
    this.lastUpdate = LocalDateTime.now();
}

    public void updateSensitiveData( 
     Optional<BloodType> newBloodType,
        Optional<DateOfBirth> newDateOfBirth,
        Optional<Document> newDni,
        Optional<String> newDocumentoEPS,
        Optional<FullName> newFullName,
        Optional<TypeGuardian> newTypeGuardian){


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

    if (newBloodType.isPresent() || newDateOfBirth.isPresent() || newDni.isPresent()
        || newDocumentoEPS.isPresent() || newFullName.isPresent()) {

        BloodType finalBloodType = newBloodType.orElse(this.person.getBloodType());
        DateOfBirth finalDateOfBirth = newDateOfBirth.orElse(this.person.getDateOfBirth());
        Document finalDni = newDni.orElse(this.person.getDni());
        String finalDocumentoEPS = newDocumentoEPS.orElse(this.person.getDocumentoEPS());
        FullName finalFullName = newFullName.orElse(this.person.getFullname());

            
  this.person = this.person.withSensitiveData(
            finalBloodType,
            finalDateOfBirth,
            finalDni,
            finalDocumentoEPS,
            finalFullName
        );
    
    }

    // Actualizar TypeGuardian si está presente
    newTypeGuardian.ifPresent(tg -> this.typeGuardian = tg);

    this.lastUpdate = LocalDateTime.now();
}
        


        public Outcome<Void> validateDeactivation() {
            if (patientList != null && !patientList.isEmpty()) {
                return Outcome.fail(new OutcomeDetail(
                        GuardianError.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS,
                        ErrorSeverity.INFO,
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
    
public static Guardian reconstruct(
        GuardianId guardianId,
        Person person,
        TypeGuardian typeGuardian,
        UserIdentityId userIdentityId,
        List<PatientId> patientList,
        LocalDateTime lastUpdate) {
    
    return new Guardian(guardianId, patientList, person, typeGuardian, userIdentityId);
}
}
