package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;
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
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final int MAX_LENGTH = 254;   // longitud total
    private static final int MAX_LOCAL = 64;     // parte local
    private static final int MAX_DOMAIN = 253;   // parte dominio

    private final String value;

    private Email(String normalized) {
        this.value = normalized;
    }

    /**
     * Fábrica segura: valida y normaliza el email.
     *
     * @param raw email de entrada (puede contener espacios).
     * @return Outcome con Email válido o detalles de error.
     */
    public static Outcome<Email> of(String raw) {
        if (raw == null) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_NULL,
                    Severity.ERROR,
                    Category.TECNICO, VOContext.AUTHORIZATION
            ));
        }

        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_EMPTY,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }

        int atIndex = trimmed.lastIndexOf('@');
        if (atIndex <= 0 || atIndex == trimmed.length() - 1) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }

        String local = trimmed.substring(0, atIndex);
        String domain = trimmed.substring(atIndex + 1);

        domain = domain.trim().toLowerCase();
        String normalized = local + "@" + domain;

        // Validaciones de longitud
       
        if (local.length() > MAX_LOCAL) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_LOCAL_LENGTH_EXCEEDED,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }
        if (domain.length() > MAX_DOMAIN) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }
        
         if (normalized.length() > MAX_LENGTH) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_LENGTH_EXCEEDED,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }

        // Validación de patrón
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_INVALID_FORMAT,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }

        // Validaciones adicionales del dominio
        if (domain.startsWith("-") || domain.endsWith("-")) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_DOMAIN_INVALID_DASH,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }
        if (domain.contains("..")) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS,
                    Severity.ERROR,
                    Category.TECNICO,VOContext.AUTHORIZATION
            ));
        }

        return Outcome.ok(new Email(normalized));
    }
    
    // Nuevo método para agregados que requieren excepción
    public static Email ofOrThrow(String email) {
        Outcome<Email> outcome = of(email);
        if (outcome.isFailure()) {
            throw new DomainAggregateException(
                VoAccesError.valueOf("EMAIL_INVALID"),
                EntityContext.COMPANY
            );
        }
        return outcome.getValue().get();
    }


    public String value() {
        return value;
    }

}
