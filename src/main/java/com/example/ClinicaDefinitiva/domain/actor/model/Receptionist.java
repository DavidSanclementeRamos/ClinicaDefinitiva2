package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Receptionist {

    private final ReceptionId id;
    private Person person;
    private Sector sector;
    private final UserIdentity user;
    private LocalDateTime lastUpdate;

    private Receptionist(ReceptionId id, Person person, Sector sector, UserIdentity user, LocalDateTime lastUpdate) {
        this.id = id;
        this.person = person;
        this.sector = sector;
        this.user = user;
        this.lastUpdate = lastUpdate;
    }


    public static Receptionist registerReceptionist(
            ReceptionId id,
            Person data,
            UserIdentity user,
            Sector sector) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER, ContextoEntidad.RECEPTIONIST);
        return new Receptionist(id, data, sector, user, LocalDateTime.now());
    }


    // Validación 3: Solo puede cancelar citas si esta activo
    public void cancelAppointment(Appointment appointment) {
        // 1. Validar que el actor está activo
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE, ContextoEntidad.RECEPTIONIST);

        // 2. Delegar la cancelación a la cita
        appointment.cancel();
    }

    // Métodos de actualización de datos
    public void updateContactData(Person data, UserIdentity user) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE, ContextoEntidad.RECEPTIONIST);
        this.person = person.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Person data, UserIdentity user, Sector sector) {
        UserStatus.from(user).mustBeActive(ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE, ContextoEntidad.RECEPTIONIST);
        this.person = data.updateSensitive(data.getAge(), data.getBloodType(),
                data.getDateOfBirth(), data.getDni(), data.getDocumentoEPS(), data.getFullname());
        this.sector = sector;
        this.lastUpdate = LocalDateTime.now();
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