package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;


/**
 * Value Object que representa el estado de una empresa.
 * Inmutable y con validaciones de negocio.
 */
public final class CompanyStatus {

    public enum Status {
        ACTIVE("Compañía activa"),
        INACTIVE("Compañía inactiva"),
        SUSPENDED("Compañía suspendida");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final Status status;

    private CompanyStatus(Status status) {
        if(status == null ){
            throw new IllegalArgumentException("no null");
        }

        this.status = status;

    }

    public static CompanyStatus of(Status status) {
        return new CompanyStatus(status);
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return status.getDescription();
    }

    // --- Reglas de negocio integradas ---
    public boolean isEditable() {
        return status == Status.ACTIVE;
    }

    public boolean isInactive() {
        return status == Status.INACTIVE;
    }

    public boolean isSuspended() {
        return status == Status.SUSPENDED;
    }

}
