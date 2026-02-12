package com.example.ClinicaDefinitiva.domain.actor.vo;




public final class Person {

    private  Document dni;
    private FullName fullname;
    private PhoneNumber phoneNumber;
    private Address address;
    private DateOfBirth dateOfBirth;
    private BloodType bloodType;
    private Age age ;
    private String documentoEPS;

    public Person() {
    }

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

    public Document getDni() {
        return dni;
    }

    public void setDni(Document dni) {
        this.dni = dni;
    }

    public FullName getFullname() {
        return fullname;
    }

    public void setFullname(FullName fullname) {
        this.fullname = fullname;
    }

    public void setDocumentoEPS(String documentoEPS) {
        this.documentoEPS = documentoEPS;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person updateContact(Address address, PhoneNumber phoneNumber) {
        return new Person(
                address,
                this.age,
                this.bloodType,
               this.dateOfBirth,
               this.dni,
               this.documentoEPS,
               this.fullname,
               phoneNumber
        );
    }

    public Person updateSensitive(Age age, BloodType bloodType, DateOfBirth dateOfBirth, Document dni, String documentoEPS, FullName fullname) {
    return  new Person(
            this.address,
            age,
            bloodType,
            dateOfBirth,
            dni,
            documentoEPS,
            fullname,
            this.phoneNumber
            );
    }

}
