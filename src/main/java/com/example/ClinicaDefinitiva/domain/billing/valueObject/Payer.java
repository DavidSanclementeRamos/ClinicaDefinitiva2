package com.example.ClinicaDefinitiva.domain.billing.valueObject;

/**
 * Value Object: Payer (Pagador de la factura)
 *
 * Reglas de negocio:
 * - RN-INVOICE-007: Todo pagador institucional (EPS, aseguradora, medicina prepagada)
 *   requiere contrato asociado.
 */
public final class Payer {
    private final String value;

    private Payer(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payer is required");
        }
        this.value = value.trim();
    }

    public static Payer of(String value) {
        return new Payer(value);
    }

    /**
     * Determina si este pagador requiere contrato.
     * Se considera que EPS, aseguradoras y medicina prepagada requieren contrato.
     */
    public boolean requiresContract() {
        String upper = value.toUpperCase();
        return upper.contains("EPS") || upper.contains("ASEGURADORA") || upper.contains("PREPAGADA");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
