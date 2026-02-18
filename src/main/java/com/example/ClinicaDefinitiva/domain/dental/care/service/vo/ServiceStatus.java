package com.example.ClinicaDefinitiva.domain.dental.care.service.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

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
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_STATUS_NULL, VOContext.DENTAL_SERVICES);
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