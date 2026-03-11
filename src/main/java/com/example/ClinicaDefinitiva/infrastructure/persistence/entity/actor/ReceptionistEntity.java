package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor;



import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "recepcionistas")
public class ReceptionistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcionista")
    private Long receptionistId;

    // Propio
    @Column(name = "sector")
    private String sector;


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
    private LocalDate dateOfBirth;
    @Column(name = "tipo_sangre")
    private String bloodType;
    @Column(name = "documento_eps")
    private String documentEPS;
    @Column(name = "usuario")
    private Long user;
    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdate;

    // Address
    @Column(name = "calle")
    private  String street;
    @Column(name = "ciudad")
    private  String city;
    @Column(name = "estado")
    private  String state;
    @Column(name = "pais")
    private  String country;
    @Column(name = "codigo_postal")
    private  String postalCode;

    public ReceptionistEntity() {
    }

    public ReceptionistEntity(Long receptionistId, String sector, String dni, String first, String lastName, String age, String phoneNumber, String address, LocalDate dateOfBirth, String bloodType, String documentEPS, Long user, LocalDateTime lastUpdate, String street, String city, String state, String country, String postalCode) {
        this.receptionistId = receptionistId;
        this.sector = sector;
        this.dni = dni;
        this.first = first;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.documentEPS = documentEPS;
        this.user = user;
        this.lastUpdate = lastUpdate;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

  

    public Long getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(Long receptionistId) {
        this.receptionistId = receptionistId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

   

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getDocumentEPS() {
        return documentEPS;
    }

    public void setDocumentEPS(String documentEPS) {
        this.documentEPS = documentEPS;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
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
