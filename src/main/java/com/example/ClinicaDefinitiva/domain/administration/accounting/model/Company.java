package com.example.ClinicaDefinitiva.domain.administration.accounting.model;


import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Nit;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.CompanyError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;

import java.time.LocalDate;
import java.util.Objects;


/**
 * Representa una empresa o entidad legal en el sistema accounting.
 * Gestiona la información fiscal, legal y de contacto de la compañía.
 */
public final class Company {

    private final CompanyId id;
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

    private Company(Builder builder) {
        validateIncorporationDate(builder.incorporationDate);
        

        this.id = builder.id;
        this.name = builder.name;
        this.taxIdentificationNumber = builder.taxIdentificationNumber;
        this.typePerson = builder.typePerson;
        this.taxRegime = builder.taxRegime;
        this.legalRepresentative = builder.legalRepresentative ;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.incorporationDate = builder.incorporationDate;
        this.status = builder.status;
    }
    
    public static Company registerCompany(
        Name name,
        Nit taxIdentificationNumber,
        TypePerson typePerson,
        TaxRegime taxRegime,
        String legalRepresentative,
        Address address,
        PhoneNumber phoneNumber,
        Email email) {

    return Company.builder()
            .withName(name)
            .withTaxIdentificationNumber(taxIdentificationNumber)
            .withTypePerson(typePerson)
            .withTaxRegime(taxRegime)
            .withLegalRepresentative(legalRepresentative)
            .withAddress(address)
            .withPhoneNumber(phoneNumber)
            .withEmail(email)
            .withIncorporationDate(LocalDate.now())
            .withStatus(CompanyStatus.of(CompanyStatus.Status.ACTIVE))
            .build();
}


   
    // Métodos de negocio
    public void updateContactInformation(Name name, String legalRepresentative,
                                         Address address, PhoneNumber phoneNumber, Email email) {
        ensureEditable();
        this.name = name;
        this.legalRepresentative = legalRepresentative != null ? legalRepresentative.trim() : null;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void updateStatus(CompanyStatus newStatus) {
        Objects.requireNonNull(newStatus, "El estado no puede ser nulo");
        if (Objects.equals(this.status, CompanyStatus.of(CompanyStatus.Status.INACTIVE)) &&
                newStatus.equals(CompanyStatus.of(CompanyStatus.Status.ACTIVE))) {
            throw new BusinessRuleViolationException(CompanyError.ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY, EntityContext.COMPANY);
        }
        this.status = newStatus;
    }

    public void updateTaxInformation(Nit taxIdentificationNumber, TaxRegime taxRegime,
                                     TypePerson typePerson, LocalDate incorporationDate) {
        ensureEditable();
        validateIncorporationDate(incorporationDate);
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.taxRegime = taxRegime;
        this.typePerson = typePerson;
        this.incorporationDate = incorporationDate;
    }

    private void ensureEditable() {
        if (!status.isEditable()) {
            throw new BusinessRuleViolationException(CompanyError.ERR_COMPANY_NOT_EDITABLE, EntityContext.COMPANY);
        }
    }

    private void validateIncorporationDate(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new BusinessRuleViolationException(CompanyError.ERR_COMPANY_MISSING_INCORPORATION_DATE, EntityContext.COMPANY);
        }
        if (date.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException(CompanyError.ERR_COMPANY_FUTURE_INCORPORATION_DATE, EntityContext.COMPANY);
        }
        if (date.isBefore(LocalDate.of(1800, 1, 1))) {
            throw new BusinessRuleViolationException(CompanyError.ERR_COMPANY_INVALID_INCORPORATION_DATE, EntityContext.COMPANY);
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
    
    
     public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CompanyId id;
        private Name name;
        private Nit taxIdentificationNumber;
        private TypePerson typePerson;
        private TaxRegime taxRegime;
        private String legalRepresentative;
        private Address address;
        private PhoneNumber phoneNumber;
        private Email email;
        private LocalDate incorporationDate = LocalDate.now();
        private CompanyStatus status = CompanyStatus.of(CompanyStatus.Status.ACTIVE);

        public Builder withId(CompanyId id) { this.id = id; return this; }
        public Builder withName(Name name) { this.name = name; return this; }
        public Builder withTaxIdentificationNumber(Nit nit) { this.taxIdentificationNumber = nit; return this; }
        public Builder withTypePerson(TypePerson typePerson) { this.typePerson = typePerson; return this; }
        public Builder withTaxRegime(TaxRegime taxRegime) { this.taxRegime = taxRegime; return this; }
        public Builder withLegalRepresentative(String rep) { this.legalRepresentative = rep; return this; }
        public Builder withAddress(Address address) { this.address = address; return this; }
        public Builder withPhoneNumber(PhoneNumber phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder withEmail(Email email) { this.email = email; return this; }
        public Builder withIncorporationDate(LocalDate date) { this.incorporationDate = date; return this; }
        public Builder withStatus(CompanyStatus status) { this.status = status; return this; }

        public Company build() {
            return new Company(this);
        }
    }

}
