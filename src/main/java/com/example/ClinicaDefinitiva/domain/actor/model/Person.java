package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Age;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.FullName;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Address;


public class Person {
    private Long id;
    private String dni;
    private FullName fullname;
    private PhoneNumber phoneNumber;
    private Address address;
    private DateOfBirth dateOfBirth;
    private BloodType bloodType;
    private Age age ;


    public Person(com.example.ClinicaDefinitiva.domain.actor.valueObject.Address address, com.example.ClinicaDefinitiva.domain.actor.valueObject.Age age, BloodType bloodType, com.example.ClinicaDefinitiva.domain.actor.valueObject.DateOfBirth dateOfBirth, String dni, com.example.ClinicaDefinitiva.domain.actor.valueObject.FullName fullname, Long id, com.example.ClinicaDefinitiva.domain.actor.valueObject.PhoneNumber phoneNumber) {
        this.address = address;
        this.age = age;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.dni = dni;
        this.fullname = fullname;
        this.id = id;
        this.phoneNumber = phoneNumber;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
