package com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject;

import java.time.LocalDate;

/**
 * Representa el intervalo de tiempo de un reporte administrativo.
 */
public final class Period {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(LocalDate startDate, LocalDate endDate) {
        return new Period(startDate, endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
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
