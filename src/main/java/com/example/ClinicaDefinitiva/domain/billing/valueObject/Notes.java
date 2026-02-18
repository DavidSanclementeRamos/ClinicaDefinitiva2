package com.example.ClinicaDefinitiva.domain.billing.valueObject;

import java.util.Optional;

/**
 * Value Object: Notes (Observaciones de la factura)
 *
 * Reglas de negocio:
 * - RN-INVOICE-009: Cancelación requiere motivo mínimo 10 caracteres.
 * - Para notas generales se valida longitud mínima opcional.
 */
public final class Notes {
    private final String value;

    private Notes(String value) {
        if (value != null && value.trim().length() < 3) {
            throw new IllegalArgumentException("Notes must have at least 3 characters");
        }
        this.value = value != null ? value.trim() : null;
    }

    public static Notes of(String value) {
        return new Notes(value);
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}

