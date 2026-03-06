package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import java.time.LocalDate;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class Period {

    private final LocalDate startDate;
    private final LocalDate endDate;

    private Period(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_PERIOD_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (endDate.isBefore(startDate)) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_PERIOD_INVALID,
                    VOContext.ACCOUNTING
            );
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(LocalDate startDate, LocalDate endDate) {
        return new Period(startDate, endDate);
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }


    /**
     * Verifica si la fecha actual está dentro del período.
     */
    public boolean isCurrentPeriod() {
        LocalDate today = LocalDate.now();
        return ( !today.isBefore(startDate) && !today.isAfter(endDate) );
    }

    /**
     * Verifica si el período terminó antes de la fecha actual.
     */
    public boolean isPastPeriod() {
        LocalDate today = LocalDate.now();
        return endDate.isBefore(today);
    }

    /**
     * Permite verificar si una fecha está dentro del periodo.
     */
    public boolean contains(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                (date.isEqual(endDate) || date.isBefore(endDate));
    }

    @Override
    public String toString() {
        return "Periodo: " + startDate + " - " + endDate;
    }
}
