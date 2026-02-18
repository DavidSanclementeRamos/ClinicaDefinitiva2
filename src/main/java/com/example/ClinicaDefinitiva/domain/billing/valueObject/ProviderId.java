package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

/**
 * Value Object: ProviderId
 *
 * Representa el identificador único de la clínica o entidad que emite la factura.
 *
 * Reglas de negocio:
 * - RN-INVOICE-014: Una factura debe tener un proveedor válido como emisor oficial.
 * - El identificador debe ser mayor a 0.
 *
 * Decisiones de diseño:
 * - Se encapsula en un VO para evitar el uso de longs planos.
 * - Permite extender en el futuro con validaciones adicionales (ej. NIT, resolución DIAN).
 */
public final class ProviderId {

    private final long value;

    private ProviderId(long value) {
        if (value <= 0) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_INVOICE_PROVIDER_REQUIRED,
                    VOContext.BILLING
            );
        }
        this.value = value;
    }

    public static ProviderId of(long value) {
        return new ProviderId(value);
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProviderId)) return false;
        ProviderId that = (ProviderId) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

