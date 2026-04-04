package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.VoError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Value Object para representar un email válido dentro del dominio.
 * Inmutable, serializable y con validación de formato.
 */
public final class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final int MAX_LENGTH = 254;
    private static final int MAX_LOCAL = 64;
    private static final int MAX_DOMAIN = 253;

    private final String value;

    // Constructor PRIVADO con VALIDACIÓN
    private Email(String normalized) {
        // La validación se hace antes de llamar al constructor
        // pero la mantenemos aquí para garantizar que cualquier instancia sea válida
        validate(normalized);
        this.value = normalized;
    }

    private static void validate(String normalized) {
        if (normalized == null) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_NULL,
                    VOContext.AUTHORIZATION
            );
        }

        String trimmed = normalized.trim();
        if (trimmed.isEmpty()) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_EMPTY,
                    VOContext.AUTHORIZATION
            );
        }

        int atIndex = trimmed.lastIndexOf('@');
        if (atIndex <= 0 || atIndex == trimmed.length() - 1) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN,
                    VOContext.AUTHORIZATION
            );
        }

        String local = trimmed.substring(0, atIndex);
        String domain = trimmed.substring(atIndex + 1).toLowerCase();

        if (local.length() > MAX_LOCAL) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_LOCAL_LENGTH_EXCEEDED,
                    VOContext.AUTHORIZATION
            );
        }
        if (domain.length() > MAX_DOMAIN) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED,
                    VOContext.AUTHORIZATION
            );
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_LENGTH_EXCEEDED,
                    VOContext.AUTHORIZATION
            );
        }

        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_INVALID_FORMAT,
                    VOContext.AUTHORIZATION
            );
        }

        if (domain.startsWith("-") || domain.endsWith("-")) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_DOMAIN_INVALID_DASH,
                    VOContext.AUTHORIZATION
            );
        }
        if (domain.contains("..")) {
            throw new ValueObjectValidationException(
                    VoError.ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS,
                    VOContext.AUTHORIZATION
            );
        }
    }

    /**
     * Fábrica que retorna Outcome (para casos donde no se quiere lanzar excepción)
     */
    public static Outcome<Email> of(String raw) {
        try {
            String trimmed = raw != null ? raw.trim() : null;
            return Outcome.ok(new Email(trimmed));
        } catch (ValueObjectValidationException e) {
            return Outcome.fail(new OutcomeDetail(
                    e.getCatalogo(),
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    VOContext.AUTHORIZATION
            ));
        }
    }

    /**
     * Fábrica que lanza excepción directamente (para mappers, servicios de aplicación)
     */
    public static Email ofOrThrow(String email) {
        String trimmed = email != null ? email.trim() : null;
        return new Email(trimmed);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
