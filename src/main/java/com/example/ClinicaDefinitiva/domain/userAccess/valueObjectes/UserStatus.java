package com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;

public class UserStatus {
    private final boolean active;

    private UserStatus(boolean active) {
        this.active = active;
    }

    // Fábrica semántica: crea el VO a partir de UserIdentity
    public static UserStatus from(UserIdentity user) {
        return new UserStatus(user.isActive());
    }

    // Regla de negocio: debe estar activo
    public void  mustBeActive(ErrorCatalog error, ContextoEntidad contexto) {
        if (!active) {
            throw new BusinessRuleViolationException(error, contexto);
        }
    }

    // Getter
    public boolean isActive() {
        return active;
    }


}
