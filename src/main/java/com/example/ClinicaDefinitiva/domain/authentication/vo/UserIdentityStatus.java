package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public final class UserIdentityStatus {

    public enum Status {
        ACTIVE("Activo"),
        INACTIVE("Inactivo"),
        SUSPENDED("Suspendido"),
        PENDING_VERIFICATION("Pendiente de verificación");

        private final String description;
        Status(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    private final Status value;

    private UserIdentityStatus(Status value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_STATUS_NULL,
                    VOContext.AUTHORIZATION
            );
        }
        this.value = value;
    }

    public static UserIdentityStatus of(Status status) {
        return new UserIdentityStatus(status);
    }

    // Queries semánticas
    public boolean isActive() { return value == Status.ACTIVE; }
    public boolean isInactive() { return value == Status.INACTIVE; }
    public boolean isSuspended() { return value == Status.SUSPENDED; }
    public boolean isPendingVerification() { return value == Status.PENDING_VERIFICATION; }

    public UserIdentityStatus transitionTo(Status next) {
        if (!canTransitionTo(next)) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_INVALID_TRANSITION,
                    VOContext.AUTHENTICATION
            );
        }
        return new UserIdentityStatus(next);
    }

    // Transiciones válidas
    public boolean canTransitionTo(Status next) {
        if (this.value == next) return false;
        return switch (this.value) {
            case PENDING_VERIFICATION -> next == Status.ACTIVE;
            case ACTIVE -> next == Status.INACTIVE || next == Status.SUSPENDED;
            case INACTIVE, SUSPENDED -> next == Status.ACTIVE;
        };
    }

    public Status getValue() { return value; }
    public String getDescription() { return value.getDescription(); }

    @Override
    public String toString() { return value.name(); }
}