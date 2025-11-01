package com.example.ClinicaDefinitiva.domain.actor.dto;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;

public class PersonRegistrationData  {
/// eliminar no sirve xd

    private Address address;
    private Age age;
    private DateOfBirth dateOfBirth;
    private BloodType bloodType;
    private FullName fullname;
    private PhoneNumber phoneNumber;
    private String dni;
    private Long id;
    private String documentoEPS;

    public PersonRegistrationData(Address address, Age age, BloodType bloodType, DateOfBirth dateOfBirth,
                                  String dni, FullName fullname, Long id, PhoneNumber phoneNumber, String documentoEPS) {
        this.address = address;
        this.age = age;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.dni = dni;
        this.fullname = fullname;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.documentoEPS = documentoEPS;
    }

    public String getDocumentoEPS() {
        return documentoEPS;
    }

    public Address getAddress() {
        return address;
    }

    public Age getAge() {
        return age;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDni() {
        return dni;
    }

    public FullName getFullname() {
        return fullname;
    }

    public Long getId() {
        return id;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }
}