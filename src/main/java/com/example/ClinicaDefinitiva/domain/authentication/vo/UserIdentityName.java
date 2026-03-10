package com.example.ClinicaDefinitiva.domain.authentication.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;

import java.io.Serial;
import java.io.Serializable;

/**
 * Value Object que representa el nombre de un usuario.
 *
 * Responsabilidades:
 * - Garantizar que el nombre cumpla reglas de validación
 * - Normalizar el nombre (trim, caso)
 * - Ser inmutable
 *
 * Reglas de negocio:
 * - Mínimo 3 caracteres
 * - Máximo 15 caracteres
 * - No puede ser null o vacío
 * - Se trimea automáticamente
 */
public final class UserIdentityName implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 15;

    private final String value;

    public UserIdentityName(String value) {
        this.value = value;
    }

    public static Outcome<UserIdentityName> create(String raw) {
        if (raw == null) {
            return Outcome.fail(new OutcomeDetail(
                    AuthenticationVoError.ERR_USER_NAME_NULL,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,VOContext.AUTHENTICATION
            ));
        }

        if (raw.isBlank()) {
            return Outcome.fail(new OutcomeDetail(
                    AuthenticationVoError.ERR_USER_NAME_EMPTY,
                    ErrorSeverity.ERROR,
                    Category.TECNICO, VOContext.AUTHENTICATION));
        }

        String trimmed = raw.trim();

        if (trimmed.length() < MIN_LENGTH) {
            return Outcome.fail(new OutcomeDetail(
                   AuthenticationVoError.ERR_USER_NAME_TOO_SHORT,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,VOContext.AUTHENTICATION
            ));
        }

        if (trimmed.length() > MAX_LENGTH) {
            return Outcome.fail(new OutcomeDetail(
                    AuthenticationVoError.ERR_USER_NAME_TOO_LONG,
                    ErrorSeverity.ERROR,
                    Category.TECNICO, VOContext.AUTHENTICATION
            ));
        }

        return Outcome.ok(new UserIdentityName(trimmed));
    }
    
    public static UserIdentityName of(String value){
        return new UserIdentityName(value);
    }

    public String getValue() {
        return value;
    }

}