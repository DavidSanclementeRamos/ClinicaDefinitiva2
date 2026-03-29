package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empresa")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String name;

    @Column(name = "nit", nullable = false, unique = true, length = 20)
    private String taxId;

    @Column(name = "tipo_persona", nullable = false, length = 20)
    private String legalEntityType;

    @Column(name = "regimen_tributario", nullable = false, length = 30)
    private String taxRegime;

    @Column(name = "representante_legal", length = 150)
    private String legalRepresentative;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String address;

    @Column(name = "telefono", length = 20)
    private String phoneNumber;

    @Column(name = "correo_electronico", length = 255)
    private String email;

    @Column(name = "fecha_constitucion", nullable = false)
    private LocalDate incorporationDate;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;

    public CompanyEntity() {}

    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getTaxId()             { return taxId; }
    public String getLegalEntityType()   { return legalEntityType; }
    public String getTaxRegime()         { return taxRegime; }
    public String getLegalRepresentative() { return legalRepresentative; }
    public String getAddress()           { return address; }
    public String getPhoneNumber()       { return phoneNumber; }
    public String getEmail()             { return email; }
    public LocalDate getIncorporationDate() { return incorporationDate; }
    public String getStatus()            { return status; }

    public void setName(String name)                       { this.name = name; }
    public void setTaxId(String taxId)                     { this.taxId = taxId; }
    public void setLegalEntityType(String legalEntityType) { this.legalEntityType = legalEntityType; }
    public void setTaxRegime(String taxRegime)             { this.taxRegime = taxRegime; }
    public void setLegalRepresentative(String legalRepresentative) { this.legalRepresentative = legalRepresentative; }
    public void setAddress(String address)                 { this.address = address; }
    public void setPhoneNumber(String phoneNumber)         { this.phoneNumber = phoneNumber; }
    public void setEmail(String email)                     { this.email = email; }
    public void setIncorporationDate(LocalDate incorporationDate) { this.incorporationDate = incorporationDate; }
    public void setStatus(String status)                   { this.status = status; }
}