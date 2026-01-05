package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;


import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyStatus;

public final class ServiceStatus {

    /**
     * Enum interno que define los estados permitidos
     */
    public enum State {
        ACTIVE,
        INACTIVE,
        DEPRECATED
    }
    private final State value;

    public ServiceStatus(State value) {
        if (value == null) {
            throw new IllegalArgumentException("ServiceStatus cannot be null");
        }
        this.value = value;
    }
    public static ServiceStatus of(State status) {
        return new ServiceStatus(status);}

    public boolean isActive() {
        return State.ACTIVE.equals(this.value);
    }

    public State getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.name();
    }


}