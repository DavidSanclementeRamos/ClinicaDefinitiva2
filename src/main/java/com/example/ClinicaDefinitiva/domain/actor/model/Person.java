package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Age;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.FullName;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Address;


public final class Person {

    private String dni;
    private FullName fullname;
    private PhoneNumber phoneNumber;
    private Address address;
    private DateOfBirth dateOfBirth;
    private BloodType bloodType;
    private Age age ;
    private String documentoEPS;


    public Person(Address address, Age age, BloodType bloodType, DateOfBirth dateOfBirth, String dni, String documentoEPS, FullName fullname, Long id, PhoneNumber phoneNumber) {
        this.address = address;
        this.age = age;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.dni = dni;
        this.documentoEPS = documentoEPS;
        this.fullname = fullname;

        this.phoneNumber = phoneNumber;
    }

    public String getDocumentoEPS() {
        return documentoEPS;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public FullName getFullname() {
        return fullname;
    }

    public void setFullname(FullName fullname) {
        this.fullname = fullname;
    }



    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
