package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

public final class ResourceCatalog {

    public enum BasicResource {
        // Actor module
        DENTIST, PATIENT, GUARDIAN, RECEPTIONIST,
        // Dental Care Services
        PROVIDED_SERVICE,
        // Schedule module
        APPOINTMENT, AVAILABILITY, SHIFT,
        // Billing module
        INVOICE, RATE, PAYMENT,
        // Accounting module
        COMPANY, CONTRACT, JOURNAL_ENTRY, LEDGER_ACCOUNT,
        THIRD_PARTIES, ADMINISTRATIVE_REPORT, OPENING_BALANCE,

        // Security module
        ROLE, PERMISSION, ASSIGNMENT
    }

    private final String code;

    private ResourceCatalog(String code) {
        this.code = code.toUpperCase();
    }

    // Factory para valores básicos
    public static ResourceCatalog of(BasicResource resource) {
        return new ResourceCatalog(resource.name());
    }

    // Factory para valores dinámicos
    public static ResourceCatalog custom(String resource) {
        return new ResourceCatalog(resource);
    }

    public String getCode() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceCatalog)) return false;
        ResourceCatalog that = (ResourceCatalog) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(code);
    }
}

