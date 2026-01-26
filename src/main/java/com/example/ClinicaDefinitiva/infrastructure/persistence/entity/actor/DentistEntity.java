package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor;

import jakarta.persistence.*;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "odontologos")
public class DentistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_odontologo")
    // del agregado
    private Long dentistId;

    @Column(name = "especialidades")
    private String specialties;

    @Column(name = "estado_disponibilidad")
    private String availabilityStatus;

    // Declaration de horas laborales
    @Column(name = "inicio_jornada")
    private LocalTime start;

    @Column(name = "fin_jornada")
    private LocalTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private DayOfWeek dayOfWeek;

    @Column(name = "horas_declaradas_por_semana")
    private int declaredHoursPerWeek;

    // Person
    @Column(name = "dni")
    private String dni;

    @Column(name = "nombre")
    private String first;

    @Column(name = "apellido")
    private String lastName;

    @Column(name = "edad")
    private String age;

    @Column(name = "telefono")
    private String phoneNumber;

    @Column(name = "fecha_nacimiento")
    private LocalDate dateOfBirth;

    @Column(name = "tipo_sangre")
    private String bloodType;

    @Column(name = "documento_eps")
    private String documentoEPS;

    @Column(name = "usuario")
    private String user;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdate;

    // Address
    @Column(name = "calle")
    private String street;

    @Column(name = "ciudad")
    private String city;

    @Column(name = "estado")
    private String state;

    @Column(name = "pais")
    private String country;

    @Column(name = "codigo_postal")
    private String postalCode;

    public DentistEntity() {
    }

    public DentistEntity(String user, String street, String state, LocalTime start, String specialties,
                         String postalCode, String phoneNumber, String lastName, LocalDateTime lastUpdate,
                         String first, LocalTime end, String documentoEPS, String dni, Long dentistId,
                         int declaredHoursPerWeek, DayOfWeek dayOfWeek, LocalDate dateOfBirth,
                         String country, String city, String bloodType, String availabilityStatus, String age) {
        this.user = user;
        this.street = street;
        this.state = state;
        this.start = start;
        this.specialties = specialties;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.lastName = lastName;
        this.lastUpdate = lastUpdate;
        this.first = first;
        this.end = end;
        this.documentoEPS = documentoEPS;
        this.dni = dni;
        this.dentistId = dentistId;
        this.declaredHoursPerWeek = declaredHoursPerWeek;
        this.dayOfWeek = dayOfWeek;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.city = city;
        this.bloodType = bloodType;
        this.availabilityStatus = availabilityStatus;
        this.age = age;
    }



    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDeclaredHoursPerWeek() {
        return declaredHoursPerWeek;
    }

    public void setDeclaredHoursPerWeek(int declaredHoursPerWeek) {
        this.declaredHoursPerWeek = declaredHoursPerWeek;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public void setDentistId(Long dentistId) {
        this.dentistId = dentistId;
    }



    public String getDocumentoEPS() {
        return documentoEPS;
    }

    public void setDocumentoEPS(String documentoEPS) {
        this.documentoEPS = documentoEPS;
    }


    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
