package com.example.ClinicaDefinitiva.domain.billing.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object: InvoiceNumber (Número de factura)
 *
 * Reglas de negocio:
 * - El número de factura debe seguir el formato PREFIJO-NÚMERO
 *   Ejemplo válido: "FAC-0001", "INV-0123"
 * - El prefijo debe ser de 2 a 5 letras mayúsculas
 * - El número debe tener entre 4 y 8 dígitos, con ceros a la izquierda
 *
 * Notas:
 * - La generación secuencial es responsabilidad del servicio InvoiceNumberGenerator.
 * - Este VO únicamente valida el formato y encapsula la semántica del número de factura.
 */
public final class InvoiceNumber {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Z]{2,5}-\\d{4,8}$");

    private final String value;

    private InvoiceNumber(String value) {
        validate(value);
        this.value = value.toUpperCase();
    }

    /**
     * Crea un InvoiceNumber a partir de un string.
     *
     * @param value número de factura en formato PREFIJO-NÚMERO (ej. "FAC-0001")
     * @return instancia de InvoiceNumber
     * @throws ValueObjectValidationException si el formato es inválido
     */
    public static InvoiceNumber of(String value) {
        return new InvoiceNumber(value);
    }

    /**
     * Crea un InvoiceNumber a partir de un prefijo y un número secuencial.
     *
     * @param prefix prefijo (ej. "FAC", "INV")
     * @param sequence número secuencial
     * @return instancia de InvoiceNumber
     */
    public static InvoiceNumber from(String prefix, long sequence) {
        if (prefix == null || prefix.isBlank()) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_INVOICE_NUMBER_PREFIX_REQUIRED,
                    VOContext.BILLING

            );
        }

        if (sequence < 0) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_INVOICE_NUMBER_NEGATIVE,
                    VOContext.BILLING

            );
        }

        String formattedNumber = String.format("%s-%04d", prefix.toUpperCase(), sequence);
        return new InvoiceNumber(formattedNumber);
    }

    /**
     * Valida que el número cumpla con el formato PREFIJO-NÚMERO.
     */
    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_INVOICE_NUMBER_REQUIRED,
                    VOContext.BILLING

            );
        }

        String normalized = value.toUpperCase().trim();

        if (!VALID_PATTERN.matcher(normalized).matches()) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_INVOICE_NUMBER_INVALID_FORMAT,
                    VOContext.BILLING
            );
        }
    }

    // ========== CONSULTAS ==========

    /** Obtiene el prefijo (ej. "FAC" de "FAC-0001"). */
    public String getPrefix() {
        return value.substring(0, value.indexOf('-'));
    }

    /** Obtiene la parte numérica como long (ej. 1 de "FAC-0001"). */
    public long getSequence() {
        String numberPart = value.substring(value.indexOf('-') + 1);
        return Long.parseLong(numberPart);
    }

    /** Verifica si el número pertenece a un prefijo específico. */
    public boolean hasPrefix(String prefix) {
        return this.getPrefix().equals(prefix.toUpperCase());
    }

    // ========== CONVERSIÓN ==========

    public String getValue() {
        return value;
    }

    // ========== IGUALDAD DE VO ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceNumber)) return false;
        InvoiceNumber that = (InvoiceNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

