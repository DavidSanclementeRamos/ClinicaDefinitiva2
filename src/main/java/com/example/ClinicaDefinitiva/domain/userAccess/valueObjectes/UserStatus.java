package com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes;

import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;

public class UserStatus {
    public final State state;

    public enum State {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        PENDING_VERIFICATION
    }

    private UserStatus(State state) {
        this.state = state;
    }

    public static UserStatus from(UserIdentity user) {
        return new UserStatus(user.isActive() ? State.ACTIVE : State.INACTIVE);
    }

    public static UserStatus from(State state) {
        return new UserStatus(state );
    }

    public void mustBeActive(ErrorCatalog error, EntityContext contexto) {
        if (state != State.ACTIVE) {
            throw new BusinessRuleViolationException(error, contexto);
        }
    }

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public State getState() {
        return state;
    }
}