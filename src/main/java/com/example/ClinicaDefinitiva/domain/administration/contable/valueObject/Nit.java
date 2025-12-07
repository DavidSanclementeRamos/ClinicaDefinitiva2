package com.example.ClinicaDefinitiva.domain.administration.contable.valueObject;


import java.io.Serial;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Value Object para representar un NIT.
 */
public final class Nit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Regex: números con opcional guion y dígito de verificación
    private static final Pattern NIT_PATTERN = Pattern.compile("^\\d{5,12}(-\\d)?$");

    private final String value;

    public Nit(String value) {
        this.value = value;
    }

    /**
     * Fábrica segura para crear un NIT válido.
     *
     * @param rawNIT   cadena con el NIT
     * @return instancia válida de Nit
     * @throws InvalidNitException si el NIT no cumple formato
     */
    public static Nit of(String rawNIT) {
        if (rawNIT == null || rawNIT.trim().isEmpty()) {
            throw new InvalidNitException("El NIT no puede ser nulo ni vacío.");
        }

        String normalized = rawNIT.trim();

        if (!NIT_PATTERN.matcher(normalized).matches()) {
            throw new InvalidNitException("Formato de NIT inválido. Ejemplo válido: 900123456-7");
        }

        return new Nit(normalized);
    }

    public String value() {
        return value;
    }

}
