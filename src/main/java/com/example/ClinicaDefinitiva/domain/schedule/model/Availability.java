package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.AvailabilityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AvailabilityId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AvailabilityStatus;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Agregado: Availability (Disponibilidad)
 * Representa un bloque de tiempo recurrente donde un dentista puede atender citas
 */
public class Availability {

    private AvailabilityId id;
    private DentistId dentistId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private AvailabilityStatus status;
    private String deactivationReason;
    private Long version;

    protected Availability() {}

    private Availability(AvailabilityId id, DentistId dentistId, DayOfWeek dayOfWeek,
                         LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.dentistId = dentistId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AvailabilityStatus.from(AvailabilityStatus.Status.FREE);
    }

    // FACTORY METHODS

    /**
     * RN-AVAIL-001: La hora de inicio debe ser anterior a la hora de fin
     * RN-AVAIL-002: No puede crearse disponibilidad con duración negativa o cero
     */
    public static Availability create(AvailabilityId id, DentistId dentistId, DayOfWeek dayOfWeek,
                                      LocalTime startTime, LocalTime endTime) {
        if (dentistId == null) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_DENTIST_REQUIRED,EntityContext.AVAILABILITY);
        }
        if (dayOfWeek == null) {
            throw new DomainAggregateException(AvailabilityError.ERR_AVAIL_DAY_REQUIRED,EntityContext.AVAILABILITY);
        }
        if (startTime == null || endTime == null) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_TIME_REQUIRED,EntityContext.AVAILABILITY);
        }
        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_INVALID_TIME_RANGE,EntityContext.AVAILABILITY);
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.isZero() || duration.isNegative()) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_ZERO_DURATION,EntityContext.AVAILABILITY );
        }

        return new Availability(id, dentistId, dayOfWeek, startTime, endTime);
    }

    // OPERACIONES DE DOMINIO

    /**
     * RN-AVAIL-004: No puede haber dos bloques que se solapen para el mismo profesional
     */
    public boolean overlapsWith(Availability other) {
        if (other == null || !this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }
        if (!this.dentistId.equals(other.dentistId)) {
            return false;
        }
        // Solapamiento: inicio1 < fin2 && fin1 > inicio2
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    /**
     * RN-AVAIL-009: No puede extenderse sobre otro bloque ya registrado
     */
    public void extend(LocalTime newEndTime, Availability... existingAvailabilities) {
        if (newEndTime == null || !this.endTime.isBefore(newEndTime)) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_EXTENSION_CONFLICT,EntityContext.AVAILABILITY);
        }

        // Validar que la extensión no cause solapamiento
        Availability extended = new Availability(this.id, this.dentistId, this.dayOfWeek,
                this.startTime, newEndTime);
        for (Availability existing : existingAvailabilities) {
            if (existing.getId().equals(this.id)) continue; // Skip self
            if (extended.overlapsWith(existing)) {
                throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_EXTENSION_CONFLICT,EntityContext.AVAILABILITY);
            }
        }

        this.endTime = newEndTime;
    }

    /**
     * RN-AVAIL-008: Desactivación requiere motivo obligatorio
     */
    public void deactivate(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_DEACTIVATION_REQUIRES_REASON, EntityContext.AVAILABILITY);
        }
        if (!status.canTransitionTo(AvailabilityStatus.Status.BLOCKED)) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_INVALID_DEACTIVATION,EntityContext.AVAILABILITY);
        }
        this.status = AvailabilityStatus.from(AvailabilityStatus.Status.BLOCKED);
        this.deactivationReason = reason;
    }

    public void activate() {
        if (!status.canTransitionTo(AvailabilityStatus.Status.FREE)) {
            throw new BusinessRuleViolationException(AvailabilityError.ERR_AVAIL_INVALID_ACTIVATION,EntityContext.AVAILABILITY);
        }
        this.status = AvailabilityStatus.from(AvailabilityStatus.Status.FREE);
        this.deactivationReason = null;
    }

    // QUERIES

    /**
     * Verifica si esta disponibilidad cubre un horario específico
     */
    public boolean covers(DayOfWeek day, LocalTime time) {
        if (!this.dayOfWeek.equals(day)) return false;
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    public boolean coversInterval(DayOfWeek day, LocalTime start, LocalTime end) {
        if (!this.dayOfWeek.equals(day)) return false;
        return !start.isBefore(this.startTime) && !end.isAfter(this.endTime);
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public int getDurationInMinutes() {
        return (int) getDuration().toMinutes();
    }

    public boolean isActive() {
        return status.isFree() || status.isBooked();
    }

    // GETTERS

    public AvailabilityId getId() { return id; }
    public DentistId getDentistId() { return dentistId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public AvailabilityStatus getStatus() { return status; }
    public String getDeactivationReason() { return deactivationReason; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}