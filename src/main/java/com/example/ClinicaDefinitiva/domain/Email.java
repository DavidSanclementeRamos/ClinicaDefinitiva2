package com.example.ClinicaDefinitiva.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar un email válido dentro del dominio.
 * Inmutable, serializable y con validación de formato.
 */
public final class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Regex pragmática: cubre la mayoría de casos reales sin ser excesivamente permisiva.
    // No intenta cubrir 100% del RFC 5322 (ese regex sería demasiado complejo).
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    // Longitudes razonables basadas en prácticas comunes
    private static final int MAX_LENGTH = 254;   // long. total de email
    private static final int MAX_LOCAL = 64;     // parte local
    private static final int MAX_DOMAIN = 253;   // parte dominio

    private final String value;

    public Email(String normalized) {
        this.value = normalized;
    }

    /**
     * Fábrica segura: valida y normaliza el email.
     *
     * @param raw email de entrada (puede contener espacios).
     * @return instancia de Email válida.
     * @throws InvalidEmailException si el formato no es válido.
     */
    public static Email of(String raw) {
        if (raw == null) {
            throw new InvalidEmailException("El email no puede ser null.");
        }

        // Normalización básica: trim y lower-case del dominio; preserva case de la parte local.
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidEmailException("El email no puede estar vacío.");
        }
        // Separar local y dominio para normalización específica
        int atIndex = trimmed.lastIndexOf('@');
        if (atIndex <= 0 || atIndex == trimmed.length() - 1) {
            throw new InvalidEmailException("El email debe contener una parte local y un dominio.");
        }

        String local = trimmed.substring(0, atIndex);
        String domain = trimmed.substring(atIndex + 1);

        // Normalización del dominio
        domain = domain.trim().toLowerCase();

        String normalized = local + "@" + domain;

        // Validaciones de longitud
        if (normalized.length() > MAX_LENGTH) {
            throw new InvalidEmailException("La longitud del email excede el máximo permitido de " + MAX_LENGTH + " caracteres.");
        }
        if (local.length() > MAX_LOCAL) {
            throw new InvalidEmailException("La parte local del email excede " + MAX_LOCAL + " caracteres.");
        }
        if (domain.length() > MAX_DOMAIN) {
            throw new InvalidEmailException("El dominio del email excede " + MAX_DOMAIN + " caracteres.");
        }

        // Validación de patrón
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new InvalidEmailException("El formato del email no es válido.");
        }

        // Validaciones adicionales del dominio (sin etiquetas consecutivas o guion indebido)
        if (domain.startsWith("-") || domain.endsWith("-")) {
            throw new InvalidEmailException("El dominio no puede iniciar ni terminar con guion.");
        }
        if (domain.contains("..")) {
            throw new InvalidEmailException("El dominio no puede contener puntos consecutivos.");
        }

        return new Email(normalized);
    }

    /**
     * Devuelve el valor normalizado.
     */
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email other = (Email) o;
        // Comparación por valor normalizado (dominio lower-case)
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
