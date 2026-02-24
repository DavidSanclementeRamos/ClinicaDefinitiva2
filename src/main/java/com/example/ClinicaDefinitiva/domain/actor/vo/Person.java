package com.example.ClinicaDefinitiva.domain.actor.vo;


import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;

public final class Person {

    private final Document dni;
    private final FullName fullname;
    private final PhoneNumber phoneNumber;
    private final Address address;
    private final DateOfBirth dateOfBirth;
    private final BloodType bloodType;
    private final Age age;
    private final String documentoEPS;

    private Person(Address address, Age age, BloodType bloodType, DateOfBirth dateOfBirth,
                   Document dni, String documentoEPS, FullName fullname, PhoneNumber phoneNumber) {
        this.address = address;
        this.age = age;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.dni = dni;
        this.documentoEPS = documentoEPS;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
    }

    public static Person of(Address address, Age age, BloodType bloodType, DateOfBirth dateOfBirth,
                            Document dni, String documentoEPS, FullName fullname, PhoneNumber phoneNumber) {
        return new Person(address, age, bloodType, dateOfBirth, dni, documentoEPS, fullname, phoneNumber);
    }

    public  Person withSensitiveData(Age age, BloodType bloodType, DateOfBirth dateOfBirth,
                                    Document dni, String documentoEPS, FullName fullname) {
        return new Person(this.address, age, bloodType, dateOfBirth, dni, documentoEPS, fullname, this.phoneNumber);
    }

    public  Person withContactData(Address address, PhoneNumber phoneNumber) {
        return new Person(address, this.age, this.bloodType, this.dateOfBirth, this.dni,
                this.documentoEPS, this.fullname, phoneNumber);
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

    public String getDocumentoEPS() {
        return documentoEPS;
    }
}
