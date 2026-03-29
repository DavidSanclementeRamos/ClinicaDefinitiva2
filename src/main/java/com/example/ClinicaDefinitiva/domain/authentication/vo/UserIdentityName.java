package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;

import java.io.Serial;
import java.io.Serializable;

public final class UserIdentityName implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 15;

    private final String value;

    // Constructor PRIVADO con VALIDACIÓN
    private UserIdentityName(String value) {
        // Validaciones aquí, en el constructor
        if (value == null) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_NAME_NULL,
                    VOContext.AUTHENTICATION
            );
        }

        if (value.isBlank()) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_NAME_EMPTY,
                    VOContext.AUTHENTICATION
            );
        }

        if (value.length() < MIN_LENGTH) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_NAME_TOO_SHORT,
                    VOContext.AUTHENTICATION
            );
        }

        if (value.length() > MAX_LENGTH) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_NAME_TOO_LONG,
                    VOContext.AUTHENTICATION
            );
        }

        this.value = value;
    }

    // create(): usa Outcome (para casos donde no quieres excepción)
    public static Outcome<UserIdentityName> create(String raw) {
        try {
            String trimmed = raw != null ? raw.trim() : null;
            return Outcome.ok(new UserIdentityName(trimmed));
        } catch (ValueObjectValidationException e) {
            // Extraer el código de error de la excepción
            ErrorCatalog errorCode = e.getCatalogo();
            return Outcome.fail(new OutcomeDetail(
                    errorCode,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    VOContext.AUTHENTICATION
            ));
        }
    }

    // of(): lanza excepción directamente (para mappers, servicios de aplicación)
    public static UserIdentityName of(String value) {
        String trimmed = value != null ? value.trim() : null;
        return new UserIdentityName(trimmed);
    }

    public String getValue() {
        return value;
    }
}