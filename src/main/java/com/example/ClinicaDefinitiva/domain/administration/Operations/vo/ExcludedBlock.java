package com.example.ClinicaDefinitiva.domain.administration.Operations.vo;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa un bloque de tiempo dentro del turno que NO está disponible
 * para atender citas (ej. almuerzo, reunión administrativa).
 */
public final class ExcludedBlock {
    private final LocalTime start;
    private final LocalTime end;
    private final String reason;

    public ExcludedBlock(LocalTime start, LocalTime end, String reason) {
        this.start = start;
        this.end = end;
        this.reason = reason;
    }

    public boolean overlapsWith(ExcludedBlock other) {
        return this.start.isBefore(other.end) && this.end.isAfter(other.start);
    }

    public boolean overlapsWith(LocalTime otherStart, LocalTime otherEnd) {
        return this.start.isBefore(otherEnd) && this.end.isAfter(otherStart);
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public String getReason() { return reason; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExcludedBlock)) return false;
        ExcludedBlock that = (ExcludedBlock) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}

