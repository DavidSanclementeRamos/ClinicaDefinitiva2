package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa una métrica financiera u operativa dentro del reporte
 */
public final class Indicator {

    private final Name name;       // Ejemplo: "Liquidez", "Rentabilidad"
    private final BigDecimal value;  // Valor numérico del indicador
    private final String unit;       // Ejemplo: "%", "USD", "COP"

    public Indicator(Name name, BigDecimal value, String unit) {

        Objects.requireNonNull(value, "El valor del indicador no puede ser nulo");
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("La unidad del indicador es obligatoria");
        }

        this.name = name;
        this.value = value;
        this.unit = unit.trim();
    }

    public static Indicator of(Name name, BigDecimal value, String unit) {
        return new Indicator(name, value, unit);
    }

    public Name getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }


}
