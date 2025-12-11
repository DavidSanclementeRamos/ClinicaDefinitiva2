package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;



import java.time.LocalDateTime;

public class Receptionist   {
    private final ReceptionId id;
    private Person person;
    private Sector sector;
    private String user;
    private LocalDateTime lastUpdate;

    public Receptionist(Builder b ) {
        this.sector = b.sector;
        this.user = b.user;
        this.id = b.id;
        this.person = b.person;
    }

    public static Receptionist registerReceptionist(ReceptionId id , Person data, String user, Sector sector){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
        return  new Builder()
                .withReceptionId(id)
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withSector(sector)
                .withUser(user)
                .buildReceptionist();
    }
    public  void updateReceptionistContactData(Person data, UserIdentity user){
        ensureActiveUser(user);

        this.person = data.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate = LocalDateTime.now();
    }
    public  void updateReceptionistSensitiveData(Person data, UserIdentity user, Sector sector){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
        this.person = data.updateSensitive( data.getAge(),
                data.getBloodType(),
                data.getDateOfBirth(),
                data.getDni(),
                data.getDocumentoEPS(),
                data.getFullname());
        this.lastUpdate = lastUpdate;
        this.sector= sector;

    }
    public void desactive(){
        ensureActiveUser(user);
        LocalDateTime lastUpdate = LocalDateTime.now();
    }
    private static void ensureActiveUser(UserIdentity user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "user=" + user);
        }
    }

    public ReceptionId getId() {
        return id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Sector getSector() {
        return sector;
    }

    public UserIdentity getUser() {
        return user;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setUser(UserIdentity user) {
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
        private PhoneNumber phoneNumber;
        private UserIdentity user;
        private LocalDateTime lastUpdate;
        private Sector sector;
        private ReceptionId id;
        private Person person;


        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullName(FullName f) { this.fullname = f; return this; }
       // public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withUser(UserIdentity u){this.user = u; return this;}
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withSector(Sector s){this.sector = s; return this;}
        public Builder withReceptionId(ReceptionId r){this.id = r; return this;}
        public Builder withPerson(Person p){this.person = p; return this;}
        public Receptionist buildReceptionist() {
            return new Receptionist(this);
        }


    }
}