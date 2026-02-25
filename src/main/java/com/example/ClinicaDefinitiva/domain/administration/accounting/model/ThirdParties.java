package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.ThirdPartiesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;

/**
 * Representa un tercero en el sistema accounting (proveedor, cliente, empleado, etc.).
 * Gestiona la información de contacto y clasificación de terceros.
 */
public final class ThirdParties {

    private static final int MIN_DOCUMENT_LENGTH = 5;
    private static final int MAX_DOCUMENT_LENGTH = 20;

    private final ThirdPartiesId partiesId;
    private final CompanyId companyId;
    private Name name;
    private final String typeDocument;
    private final String documentNumber;
    private final TypeThirdParties typeThirdParties;
    private Address address;
    private PhoneNumber phoneNumber;
    private Email email;
    private boolean active;

    private ThirdParties(Builder builder) {
        validateMandatoryFields(builder.typeDocument, builder.documentNumber, builder.typeThirdParties);
        validateDocumentNumber(builder.documentNumber);

        this.partiesId = builder.partiesId;
        this.companyId = builder.companyId;
        this.name = builder.name;
        this.typeDocument = builder.typeDocument.trim().toUpperCase();
        this.documentNumber = builder.documentNumber.trim();
        this.typeThirdParties = builder.typeThirdParties;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.active = builder.active;
    }

    // -------- Factory method --------
    public static ThirdParties registerThirdParties(
            CompanyId companyId,
            Name name,
            String typeDocument,
            String documentNumber,
            TypeThirdParties typeThirdParties,
            Address address,
            PhoneNumber phoneNumber,
            Email email) {

        return ThirdParties.builder()
                .withCompanyId(companyId)
                .withName(name)
                .withTypeDocument(typeDocument)
                .withDocumentNumber(documentNumber)
                .withTypeThirdParties(typeThirdParties)
                .withAddress(address)
                .withPhoneNumber(phoneNumber)
                .withEmail(email)
                .withActive(true)
                .build();
    }

    // -------- Métodos de negocio --------
    public void updateContactInformation(
            Name name,
            Address address,
            PhoneNumber phoneNumber,
            Email email) {

        ensureActive();

        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void activate() {
        if (this.active) {
            throw new DomainAggregateException(ThirdPartiesError.ERR_THIRD_PARTY_ALREADY_ACTIVE, EntityContext.THISPARTIES);
        }
        this.active = true;
    }

    public void inactivate(String reason) {
        if (!this.active) {
            throw new BusinessRuleViolationException(ThirdPartiesError.ERR_THIRD_PARTY_ALREADY_INACTIVE, EntityContext.THISPARTIES);
        }
        if (reason == null || reason.isBlank()) {
            throw new DomainAggregateException(ThirdPartiesError.ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON, EntityContext.THISPARTIES);
        }
        this.active = false;
    }

    public boolean canPerformTransactions() {
        return this.active;
    }

    public boolean isSupplier() {
        return this.typeThirdParties == TypeThirdParties.PROVEEDOR;
    }

    public boolean isCustomer() {
        return this.typeThirdParties == TypeThirdParties.CLIENTE;
    }

    public boolean isEmployee() {
        return this.typeThirdParties == TypeThirdParties.EMPLEADO;
    }

    // -------- Validaciones internas --------
    private void ensureActive() {
        if (!this.active) {
            throw new BusinessRuleViolationException(ThirdPartiesError.ERR_THIRD_PARTY_NOT_EDITABLE, EntityContext.THISPARTIES);
        }
    }

    private void validateMandatoryFields(
            String typeDocument,
            String documentNumber,
            TypeThirdParties typeThirdParties) {

        if (typeDocument == null || typeDocument.isBlank()) {
            throw new DomainAggregateException(ThirdPartiesError.ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE, EntityContext.THISPARTIES);
        }
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new DomainAggregateException(ThirdPartiesError.ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER, EntityContext.THISPARTIES);
        }
        if (typeThirdParties == null) {
            throw new DomainAggregateException(ThirdPartiesError.ERR_THIRD_PARTY_MISSING_TYPE, EntityContext.THISPARTIES);
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        String cleanDocument = documentNumber.trim().replaceAll("[^0-9A-Za-z]", "");
        if (cleanDocument.length() < MIN_DOCUMENT_LENGTH || cleanDocument.length() > MAX_DOCUMENT_LENGTH) {
            throw new BusinessRuleViolationException(ThirdPartiesError.ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH, EntityContext.THISPARTIES);
        }
    }

    // -------- Getters --------
    public ThirdPartiesId getPartiesId() { return partiesId; }
    public CompanyId getCompanyId() { return companyId; }
    public Name getName() { return name; }
    public String getTypeDocument() { return typeDocument; }
    public String getDocumentNumber() { return documentNumber; }
    public TypeThirdParties getTypeThirdParties() { return typeThirdParties; }
    public Address getAddress() { return address; }
    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    public Email getEmail() { return email; }
    public boolean isActive() { return active; }

    // -------- Builder interno --------
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private ThirdPartiesId partiesId;
        private CompanyId companyId;
        private Name name;
        private String typeDocument;
        private String documentNumber;
        private TypeThirdParties typeThirdParties;
        private Address address;
        private PhoneNumber phoneNumber;
        private Email email;
        private boolean active;

        public Builder withPartiesId(ThirdPartiesId partiesId) { this.partiesId = partiesId; return this; }
        public Builder withCompanyId(CompanyId companyId) { this.companyId = companyId; return this; }
        public Builder withName(Name name) { this.name = name; return this; }
        public Builder withTypeDocument(String typeDocument) { this.typeDocument = typeDocument; return this; }
        public Builder withDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; return this; }
        public Builder withTypeThirdParties(TypeThirdParties typeThirdParties) { this.typeThirdParties = typeThirdParties; return this; }
        public Builder withAddress(Address address) { this.address = address; return this; }
        public Builder withPhoneNumber(PhoneNumber phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder withEmail(Email email) { this.email = email; return this; }
        public Builder withActive(boolean active) { this.active = active; return this; }

        public ThirdParties build() { return new ThirdParties(this); }
    }
}
