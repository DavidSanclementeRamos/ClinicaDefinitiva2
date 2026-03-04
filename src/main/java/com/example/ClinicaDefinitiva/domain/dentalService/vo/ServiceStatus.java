package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;

public final class ServiceStatus {

    /**
     * Enum interno que define los estados permitidos con descripción legible
     */
    public enum State {
        ACTIVE("Activo"),
        INACTIVE("Inactivo"),
        DEPRECATED("Obsoleto");

        private final String description;

        State(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final State value;

    private ServiceStatus(State value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                ServiceVOError.ERR_SERVICE_STATUS_NULL,
                VOContext.DENTAL_SERVICES
            );
        }
        this.value = value;
    }

    public static ServiceStatus of(State status) {
        return new ServiceStatus(status);
    }

    public boolean isActive() {
        return State.ACTIVE.equals(this.value);
    }

    public State getValue() {
        return value;
    }

    public String getDescription() {
        return value.getDescription();
    }

    @Override
    public String toString() {
        return value.name() + " (" + value.getDescription() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceStatus)) return false;
        ServiceStatus that = (ServiceStatus) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}