
package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

/**
 * Value Object Person mapeado como @Embeddable.
 *
 * Decisión ADR-54: las columnas de persona se embeben directamente en
 * la tabla del actor (dentista, paciente, responsable, recepcionista)
 * para evitar JOIN innecesario en lecturas frecuentes.
 */
@Embeddable
public class PersonEmbeddable {

    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String fullName;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String documentType;

    @Column(name = "numero_documento", nullable = false, length = 30)
    private String documentNumber;

    @Column(name = "tipo_sangre", length = 5)
    private String bloodType;

    @Column(name = "fecha_nacimiento")
    private LocalDate birthDate;

    @Column(name = "documento_eps", length = 50)
    private String epsDocument;

    @Column(name = "telefono", length = 20)
    private String phoneNumber;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String address;

    public PersonEmbeddable() {}

    public String getFullName()        { return fullName; }
    public String getDocumentType()     { return documentType; }
    public String getDocumentNumber()   { return documentNumber; }
    public String getBloodType()        { return bloodType; }
    public LocalDate getBirthDate()     { return birthDate; }
    public String getEpsDocument()      { return epsDocument; }
    public String getPhoneNumber()      { return phoneNumber; }
    public String getAddress()          { return address; }

    public void setFullName(String fullName)               { this.fullName = fullName; }
    public void setDocumentType(String documentType)       { this.documentType = documentType; }
    public void setDocumentNumber(String documentNumber)   { this.documentNumber = documentNumber; }
    public void setBloodType(String bloodType)             { this.bloodType = bloodType; }
    public void setBirthDate(LocalDate birthDate)          { this.birthDate = birthDate; }
    public void setEpsDocument(String epsDocument)         { this.epsDocument = epsDocument; }
    public void setPhoneNumber(String phoneNumber)         { this.phoneNumber = phoneNumber; }
    public void setAddress(String address)                 { this.address = address; }
}