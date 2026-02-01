package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.Operations.ShiftId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import java.time.LocalDateTime;

public class Receptionist   {

    private final ReceptionId id;
    private final ShiftId shiftId;
    private Person person;
    private Sector sector;
    private final UserId userId;
    private LocalDateTime lastUpdate;

    public Receptionist(LocalDateTime lastUpdate, UserId userId, Sector sector, Person person, ShiftId shiftId, ReceptionId id) {
        this.lastUpdate = lastUpdate;
        this.userId = userId;
        this.sector = sector;
        this.person = person;
        this.shiftId = shiftId;
        this.id = id;
    }

    public static Receptionist registerReceptionist(
            Person data,
            UserId userId,
            Sector sector,
            ShiftId shiftId) {
        return new Receptionist(LocalDateTime.now(), userId , sector, data,shiftId, null);
    }


    // DELEGAR A UN SERVICE LA EVALUACION DE PODER CANCELAR, ALGO PARECIDO A USER
   /** // Validación 3: Solo puede cancelar citas si esta activo
    public void cancelAppointment(Appointment appointment) {
        // 1. Validar que el actor está activo

        // 2. Delegar la cancelación a la cita
        appointment.cancel();
    }*/

    public void updateContactData(Address address, PhoneNumber phoneNumber) {
        Person data = new Person();
        this.person = data.updateContact(address, phoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni,
                                    String documentoEPS, FullName fullname, Sector sector) {
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


    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ReceptionId getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Sector getSector() {
        return sector;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }


}