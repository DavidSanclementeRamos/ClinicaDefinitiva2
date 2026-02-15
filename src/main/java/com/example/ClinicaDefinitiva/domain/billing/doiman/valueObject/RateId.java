package com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject;

import java.util.Objects;
import java.util.UUID;

public final class RateId {
    private final Long value;

    public RateId(Long value) {
        this.value = Objects.requireNonNull(value);
    }
    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static RateId of(Long value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres

        return new RateId(value);
    }
    public Long getValue() {
        return value;
    }
}
