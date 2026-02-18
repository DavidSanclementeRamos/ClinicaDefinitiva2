package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import java.util.Currency;

/**
 * Value Object: CurrencyCode (Código ISO 4217)
 *
 * Reglas de negocio:
 * - RN-INVOICE-008: Todos los ítems deben tener la misma moneda que la factura.
 * - Se valida contra ISO 4217.
 */
public final class CurrencyCode {
    private final String code;

    private CurrencyCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
        try {
            Currency.getInstance(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + code);
        }
        this.code = code.toUpperCase();
    }

    public static CurrencyCode of(String code) {
        return new CurrencyCode(code);
    }

    public Currency toJavaCurrency() {
        return Currency.getInstance(code);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}

