package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tercero")
public class ThirdPartyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity company;

    @Column(name = "nombre", nullable = false, length = 200)
    private String name;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String documentType;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String documentNumber;

    @Column(name = "tipo_tercero", nullable = false, length = 30)
    private String thirdPartyType;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String address;

    @Column(name = "telefono", length = 20)
    private String phoneNumber;

    @Column(name = "correo_electronico", length = 255)
    private String email;

    @Column(name = "activo", nullable = false)
    private boolean active;

    public ThirdPartyEntity() {}

    public Long getId()                  { return id; }
    public CompanyEntity getCompany()     { return company; }
    public String getName()               { return name; }
    public String getDocumentType()       { return documentType; }
    public String getDocumentNumber()     { return documentNumber; }
    public String getThirdPartyType()     { return thirdPartyType; }
    public String getAddress()            { return address; }
    public String getPhoneNumber()        { return phoneNumber; }
    public String getEmail()              { return email; }
    public boolean isActive()             { return active; }

     public void setId(Long id) {
        this.id = id;
    }
    public void setCompany(CompanyEntity company)        { this.company = company; }
    public void setName(String name)                     { this.name = name; }
    public void setDocumentType(String documentType)     { this.documentType = documentType; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public void setThirdPartyType(String thirdPartyType) { this.thirdPartyType = thirdPartyType; }
    public void setAddress(String address)               { this.address = address; }
    public void setPhoneNumber(String phoneNumber)       { this.phoneNumber = phoneNumber; }
    public void setEmail(String email)                   { this.email = email; }
    public void setActive(boolean active)                { this.active = active; }
}