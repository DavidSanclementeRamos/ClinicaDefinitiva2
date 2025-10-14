package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.dto.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;


import java.time.LocalDateTime;

public class Receptionist extends Person {
    private Sector sector;
    private UserModel user;

    public Receptionist(Builder b ) {
        super(b.address, b.age, b.bloodType, b.dateOfBirth, b.dni, b.fullname, b.id, b.phoneNumber);
        this.sector = b.sector;
        this.user = b.user;
    }

    public static Receptionist registerReceptionist(PersonRegistrationData data, UserModel user, Sector sector){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
        return  new Builder()
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withId(data.getId())
                .withSector(sector)
                .withUser(user)
                .buildReceptionist();
    }
    public static Receptionist updateReceptionistContactData(PersonRegistrationData data, UserModel user){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
        return  new Builder()
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .withUser(user)
                .buildReceptionist();
    }
    public static Receptionist updateReceptionistSensitiveData(PersonRegistrationData data, UserModel user, Sector sector){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
        return  new Builder()
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withLastUpdate(lastUpdate)
                .withUser(user)
                .buildReceptionist();
    }
    public void desactive(){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
    }
    private static void ensureActiveUser(UserModel user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "user=" + user);
        }
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    // Builder estático
    public static final class Builder {
        private Address address;
        private Age age;
        private BloodType bloodType;
        private DateOfBirth dateOfBirth;
        private String dni;
        private FullName fullname;
        private Long id;
        private PhoneNumber phoneNumber;
        private UserModel user;
        private LocalDateTime lastUpdate;
        private Sector sector;


        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullName(FullName f) { this.fullname = f; return this; }
        public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withUser(UserModel u){this.user = u; return this;}
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withSector(Sector s){this.sector = s; return this;}
        public Receptionist buildReceptionist() {
            return new Receptionist(this);
        }


    }
}