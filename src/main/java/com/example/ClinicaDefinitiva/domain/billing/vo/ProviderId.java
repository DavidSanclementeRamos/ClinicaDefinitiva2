package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

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
public  record ProviderId(Long getValue) {

    public static ProviderId of(Long value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                    BillingVOError.ERR_INVOICE_PROVIDER_REQUIRED,
                    VOContext.BILLING
            );
        }
        return new ProviderId(value);
    }

   }

