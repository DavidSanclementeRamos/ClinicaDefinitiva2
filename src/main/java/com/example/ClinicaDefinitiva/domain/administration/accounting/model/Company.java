package com.example.ClinicaDefinitiva.domain.administration.accounting.model;


import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Address;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Nit;
import java.time.LocalDate;
import java.util.Objects;


/**
 * Representa una empresa o entidad legal en el sistema accounting.
 * Gestiona la información fiscal, legal y de contacto de la compañía.
 */
public final class Company {

    private CompanyId id;
    private Name name;
    private Nit taxIdentificationNumber;
    private TypePerson typePerson;
    private TaxRegime taxRegime;
    private String legalRepresentative;
    private Address address;
    private PhoneNumber phoneNumber;
    private Email email;
    private LocalDate incorporationDate;
    private CompanyStatus status;

    private Company(
            CompanyId id,
            Name name,
            Nit taxIdentificationNumber,
            TypePerson typePerson,
            TaxRegime taxRegime,
            String legalRepresentative,
            Address address,
            PhoneNumber phoneNumber,
            Email email,
            LocalDate incorporationDate,
            CompanyStatus status
            ) {


        if (Objects.isNull(typePerson)) {
            throw new InvalidCompanyException("El tipo de persona es obligatorio");
        }

        if (Objects.isNull(incorporationDate)) {
            throw new InvalidCompanyException("La fecha de constitución es obligatoria");
        }
        if (incorporationDate.isAfter(LocalDate.now())) {
            throw new InvalidCompanyException("La fecha de constitución no puede ser futura");
        }

        this.id = id;
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.typePerson = typePerson;
        this.taxRegime = taxRegime;
        this.legalRepresentative = legalRepresentative != null ? legalRepresentative.trim() : null;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.incorporationDate = incorporationDate;
        this.status = status != null ? status : CompanyStatus.of(CompanyStatus.Status.ACTIVE);

    }

    /**
     * Factory method para registrar una nueva compañía.
     */
    public static Company registerCompany(
            Name name,
            Nit taxIdentificationNumber,
            TypePerson typePerson,
            TaxRegime taxRegime,
            String legalRepresentative,
            Address address,
            PhoneNumber phoneNumber,
            Email email) {

        return new Company(
                null,
                name,
                taxIdentificationNumber,
                typePerson,
                taxRegime,
                legalRepresentative,
                address,
                phoneNumber,
                email,
                LocalDate.now(),
                CompanyStatus.of(CompanyStatus.Status.ACTIVE)
        );
    }

    /**
     * Actualiza la información de contacto y datos generales de la compañía.
     * Solo permite edición si el estado es editable.
     */
    public void updateContactInformation(
            Name name,
            String legalRepresentative,
            Address address,
            PhoneNumber phoneNumber,
            Email email) {

        ensureEditable();

        this.name = name;
        this.legalRepresentative = legalRepresentative != null ? legalRepresentative.trim() : null;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Actualiza el estado de la compañía.
     */
    public void updateStatus(CompanyStatus newStatus) {
        Objects.requireNonNull(newStatus, "El estado no puede ser nulo");

        if (Objects.equals(this.status, CompanyStatus.of(CompanyStatus.Status.INACTIVE)) &&
                newStatus.equals(CompanyStatus.of(CompanyStatus.Status.ACTIVE))) {
            throw new InvalidCompanyStatusException(
                    "No se puede reactivar una compañía inactiva sin un proceso de reactivación formal"
            );
        }

        this.status = newStatus;
    }

    /**
     * Actualiza información fiscal de la compañía.
     * Solo permite cambios si el estado es editable.
     */
    public void updateTaxInformation(
            Nit taxIdentificationNumber,
            TaxRegime taxRegime,
            TypePerson typePerson,
            LocalDate incorporationDate) {

        ensureEditable();
        validateIncorporationDate(incorporationDate);

        this.taxIdentificationNumber = taxIdentificationNumber;
        this.taxRegime = taxRegime;
        this.typePerson = typePerson;
        this.incorporationDate = incorporationDate;
    }



    private void ensureEditable() {
        if (!status.isEditable()) {
            throw new InvalidCompanyStatusException(
                    "No se puede editar la compañía en estado " + status.getStatus()
            );
        }
    }




    // Getters
    public CompanyId getId() { return id; }
    public Name getName() { return name; }
    public Nit getTaxIdentificationNumber() { return taxIdentificationNumber; }
    public TypePerson getTypePerson() { return typePerson; }
    public TaxRegime getTaxRegime() { return taxRegime; }
    public String getLegalRepresentative() { return legalRepresentative; }
    public Address getAddress() { return address; }
    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    public Email getEmail() { return email; }
    public LocalDate getIncorporationDate() { return incorporationDate; }
    public CompanyStatus getStatus() { return status; }

    // Setters para infraestructura (JPA/Hibernate)
    public void setId(CompanyId id) { this.id = id; }
}