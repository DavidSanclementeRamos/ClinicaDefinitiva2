package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.TypeGuardian;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guardianes")
public class GuardianEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_guardian")
    private Long guardianId;

    // Propio
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tutor")
    private TypeGuardian typeGuardian;

    // Guardamos referencia al id del horario para simplificar (evitar dependencia directa a Schedule entity)
    @Column(name = "id_horario")
    private Long scheduleId;

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientEntity> patientList = new ArrayList<>();

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

    @Column(name = "direccion")
    private String address;

    @Column(name = "fecha_nacimiento")
    private String dateOfBirth;

    @Column(name = "tipo_sangre")
    private String bloodType;

    @Column(name = "documento_eps")
    private String documentEPS;

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

    public GuardianEntity() {
    }

    // Getters y setters
    public Long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Long guardianId) {
        this.guardianId = guardianId;
    }

    public TypeGuardian getTypeGuardian() {
        return typeGuardian;
    }

    public void setTypeGuardian(TypeGuardian typeGuardian) {
        this.typeGuardian = typeGuardian;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<PatientEntity> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<PatientEntity> patientList) {
        this.patientList = patientList;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentEPS() {
        return documentEPS;
    }

    public void setDocumentEPS(String documentEPS) {
        this.documentEPS = documentEPS;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
