package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.util.*;

import java.time.LocalDateTime;

public class Receptionist  implements Actor {

    private final ReceptionId id;
    private Person person;
    private Sector sector;
    private final UserId user;
    private LocalDateTime lastUpdate;

    public Receptionist(ReceptionId id, Person person, Sector sector, UserId user, LocalDateTime lastUpdate) {
        this.id = id;
        this.person = person;
        this.sector = sector;
        this.user = user;
        this.lastUpdate = lastUpdate;
    }


    public static Receptionist registerReceptionist(
            Person data,
            UserIdentity user,
            Sector sector) {
        return new Receptionist(null, data, sector, user.getId(), LocalDateTime.now());
    }


    // Validación 3: Solo puede cancelar citas si esta activo
    public void cancelAppointment(Appointment appointment) {
        // 1. Validar que el actor está activo

        // 2. Delegar la cancelación a la cita
        appointment.cancel();
    }

    // Métodos de actualización de datos
    public void updateContactData(Address address, PhoneNumber phoneNumber, UserIdentity user) {
        // Validar que el usuario esté activo (consistencia con Dentist)
        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
        // this.sector = sector;  // no aplica aquí
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, UserIdentity user, Sector sector) {
        // Validar que el usuario esté activo (consistencia con Dentist)
        Person data = new Person();
        this.person = data.updateSensitive(
                age,
                bloodType,
                dateOfBirth,
                dni,
                documentoEPS,
                fullname);
        this.lastUpdate = LocalDateTime.now();
        this.sector = sector;
    }

    @Override
    public Outcome<H> assertCanBeDeactivated(String reason) {

        if(reason == null || reason.isBlank()){
            return Outcome.fail(new OutcomeDetail(ErrorCatalogXD.EER_RECEPTIONIST_INACTIVATION_REQUIRES_REASON, Severity.INFO, Category.ADMINISTRATIVO));
        }
        UserStatus.from(user).mustBeActive(ErrorCatalogXD.ERR_RECEPTIONIST_NOT_EDITABLE, EntityContext.RECEPTIONIST);

        return Outcome.ok();
    }

    @Override
    public void marcarInativo() {

    }

    // Getters
    public ReceptionId getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Sector getSector() {
        return sector;
    }

    public UserIdentity getUser() {
        return user;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }


}