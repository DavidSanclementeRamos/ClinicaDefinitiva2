package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.math.BigDecimal;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

public final class Indicator {

    private final String name;
    private final BigDecimal value;
    private final String unit;

    private Indicator(String name, BigDecimal value, String unit) {
        if (name == null || name.isBlank()) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_REPORT_INDICATOR_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_REPORT_INDICATOR_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_REPORT_INDICATOR_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        if (unit == null || unit.isBlank()) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_REPORT_INDICATOR_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.name = name.trim();
        this.value = value;
        this.unit = unit.trim();
    }

    public static Indicator of(String name, BigDecimal value, String unit) {
        return new Indicator(name, value, unit);
    }

    public String getName() { return name; }
    public BigDecimal getValue() { return value; }
    public String getUnit() { return unit; }
}

