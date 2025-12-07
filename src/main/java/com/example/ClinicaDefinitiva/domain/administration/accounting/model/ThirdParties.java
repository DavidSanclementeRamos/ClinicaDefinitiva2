package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Address;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;

/**
 * Representa un tercero en el sistema accounting (proveedor, cliente, empleado, etc.).
 * Gestiona la información de contacto y clasificación de terceros.
 */
public final class ThirdParties {

    private static final int MIN_DOCUMENT_LENGTH = 5;
    private static final int MAX_DOCUMENT_LENGTH = 20;

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

    private ThirdParties(
            ThirdPartiesId partiesId,
            CompanyId companyId,
            Name name,
            String typeDocument,
            String documentNumber,
            TypeThirdParties typeThirdParties,
            Address address,
            PhoneNumber phoneNumber,
            Email email,
            boolean active
            ) {

        validateMandatoryFields(typeDocument, documentNumber, typeThirdParties);
        validateDocumentNumber(documentNumber);

        this.partiesId = partiesId;
        this.companyId = companyId;
        this.name = name;
        this.typeDocument = typeDocument.trim().toUpperCase();
        this.documentNumber = documentNumber.trim();
        this.typeThirdParties = typeThirdParties;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.active = active;
    }

    /**
     * Factory method para registrar un nuevo tercero.
     */
    public static ThirdParties registerThirdParties(
            CompanyId companyId,
            Name name,
            String typeDocument,
            String documentNumber,
            TypeThirdParties typeThirdParties,
            Address address,
            PhoneNumber phoneNumber,
            Email email) {


        return new ThirdParties(
                null,
                companyId,
                name,
                typeDocument,
                documentNumber,
                typeThirdParties,
                address,
                phoneNumber,
                email,
                true

        );
    }

    /**
     * Actualiza la información de contacto del tercero.
     */
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

    /**
     * Activa el tercero para permitir operaciones.
     */
    public void activate() {
        if (this.active) {
            throw new InvalidThirdPartiesException("El tercero ya está activo");
        }
        this.active = true;
    }

    /**
     * Inactiva el tercero. Los terceros inactivos no pueden realizar operaciones.
     */
    public void inactivate(String reason) {
        if (!this.active) {
            throw new InvalidThirdPartiesException("El tercero ya está inactivo");
        }
        if (reason == null || reason.isBlank()) {
            throw new InvalidThirdPartiesException("Se requiere una razón para inactivar el tercero");
        }
        this.active = false;
    }

    /**
     * Verifica si el tercero puede realizar transacciones.
     */
    public boolean canPerformTransactions() {
        return this.active;
    }

    /**
     * Verifica si el tercero es un proveedor.
     */
    public boolean isSupplier() {
        return this.typeThirdParties == TypeThirdParties.PROVEEDOR;
    }

    /**
     * Verifica si el tercero es un cliente.
     */
    public boolean isCustomer() {
        return this.typeThirdParties == TypeThirdParties.CLIENTE;
    }

    /**
     * Verifica si el tercero es un empleado.
     */
    public boolean isEmployee() {
        return this.typeThirdParties == TypeThirdParties.EMPLEADO;
    }


    private void ensureActive() {
        if (!this.active) {
            throw new InvalidThirdPartiesException(
                    "No se puede modificar un tercero inactivo"
            );
        }
    }

    private void validateMandatoryFields(
            String typeDocument,
            String documentNumber,
            TypeThirdParties typeThirdParties) {


        if (typeDocument == null || typeDocument.isBlank()) {
            throw new InvalidThirdPartiesException("El tipo de documento es obligatorio");
        }
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new InvalidThirdPartiesException("El número de documento es obligatorio");
        }
        if (typeThirdParties == null) {
            throw new InvalidThirdPartiesException("El tipo de tercero es obligatorio");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        String cleanDocument = documentNumber.trim().replaceAll("[^0-9A-Za-z]", "");
        if (cleanDocument.length() < MIN_DOCUMENT_LENGTH || cleanDocument.length() > MAX_DOCUMENT_LENGTH) {
            throw new InvalidThirdPartiesException(
                    String.format("El número de documento debe tener entre %d y %d caracteres",
                            MIN_DOCUMENT_LENGTH, MAX_DOCUMENT_LENGTH)
            );
        }
    }

    // Getters
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

    // Setters para infraestructura
    public void setPartiesId(ThirdPartiesId partiesId) { this.partiesId = partiesId; }
}