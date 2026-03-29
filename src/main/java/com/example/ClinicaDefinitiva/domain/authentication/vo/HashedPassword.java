package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO que encapsula el hash de la contraseña y evita que se use un String crudo en el dominio.
 * Responsabilidad: garantizar que siempre se guarde un hash válido, nunca texto plano.
 */
public final class HashedPassword implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String hash;

    // Constructor PRIVADO con VALIDACIÓN
    private HashedPassword(String hash) {
        validate(hash);
        this.hash = hash;
    }

    private static void validate(String hash) {
        if (hash == null) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_PASSWORD_HASH_NULL,
                    VOContext.AUTHENTICATION
            );
        }
        if (hash.isBlank()) {
            throw new ValueObjectValidationException(
                    AuthenticationVoError.ERR_USER_PASSWORD_HASH_EMPTY,
                    VOContext.AUTHENTICATION
            );
        }
    }

    /**
     * Fábrica que retorna Outcome (para casos donde no se quiere lanzar excepción)
     */
    public static Outcome<HashedPassword> fromHash(String rawHash) {
        try {
            String trimmed = rawHash != null ? rawHash.trim() : null;
            return Outcome.ok(new HashedPassword(trimmed));
        } catch (ValueObjectValidationException e) {
            return Outcome.fail(new OutcomeDetail(
                    e.getCatalogo(),
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    VOContext.AUTHENTICATION
            ));
        }
    }

    /**
     * Fábrica que lanza excepción directamente (para mappers, servicios de aplicación)
     */
    public static HashedPassword of(String rawHash) {
        String trimmed = rawHash != null ? rawHash.trim() : null;
        return new HashedPassword(trimmed);
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return hash;
    }
}