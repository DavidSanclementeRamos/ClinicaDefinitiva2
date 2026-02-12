package com.example.ClinicaDefinitiva.domain.actor.vo;




public final class Person {

    private final Document dni;
    private final FullName fullname;
    private final PhoneNumber phoneNumber;
    private final Address address;
    private final DateOfBirth dateOfBirth;
    private final BloodType bloodType;
    private final Age age ;
    private final String documentoEPS;




    private Person(Address address, Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni, String documentoEPS, FullName fullname, PhoneNumber phoneNumber) {
        this.address = address;
        this.age = age;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.dni = dni;
        this.documentoEPS = documentoEPS;
        this.fullname = fullname;

        this.phoneNumber = phoneNumber;
    }

    public static Person of(Address address, Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni, String documentoEPS, FullName fullname, PhoneNumber phoneNumber) { return new Person(address, age, bloodType, dateOfBirth, dni, documentoEPS, fullname, phoneNumber); }
    public String getDocumentoEPS() {
        return documentoEPS;
    }

    public Document getDni() {
        return dni;
    }

    public FullName getFullname() {
        return fullname;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public Age getAge() {
        return age;
    }
}
