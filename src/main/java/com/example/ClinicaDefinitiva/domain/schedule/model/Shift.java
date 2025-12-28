package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.ShiftId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.ShiftStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.ShiftType;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Agregado: Shift (Turno Operativo)
 * Representa la presencia física del profesional en la clínica
 * Diferencia clave con Availability: Shift es presencia física, Availability es horario de atención
 */
public class Shift {

    private ShiftId id;
    private DentistId dentistId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftType type;
    private ShiftStatus status;
    private String cancellationReason;
    private Long version;
    private static final Duration MODIFICATION_WINDOW = Duration.ofHours(24);

    protected Shift() {
    }

    private Shift(ShiftId id, DentistId dentistId, LocalDate date,
                  LocalTime startTime, LocalTime endTime, ShiftType type) {
        this.id = id;
        this.dentistId = dentistId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = ShiftStatus.active();
    }

    // FACTORY METHODS

    /**
     * RN-SHIFT-001: La hora de inicio debe ser anterior a la hora de fin
     * RN-SHIFT-008: No puede tener duración negativa o cero
     */
    public static Shift create(DentistId dentistId, LocalDate date,
                               LocalTime startTime, LocalTime endTime, ShiftType type) {
        if (dentistId == null) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_DENTIST_REQUIRED,EntityContext.SHIFT);
        }
        if (date == null) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_DATE_REQUIRED,EntityContext.SHIFT);
        }
        if (startTime == null || endTime == null) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_TIME_REQUIRED,EntityContext.SHIFT );
        }
        if (type == null) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_TYPE_REQUIRED,EntityContext.SHIFT);
        }



        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_INVALID_TIME_RANGE, EntityContext.SHIFT);
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.isZero() || duration.isNegative()) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_ZERO_DURATION, EntityContext.SHIFT);
        }

        return new Shift(null, dentistId, date, startTime, endTime, type);
    }

    // OPERACIONES DE DOMINIO

    /**
     * RN-SHIFT-003: No puede solaparse con otro turno del mismo profesional
     */
    public boolean overlapsWith(Shift other) {
        if (other == null){
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_OVERLAP_TARGET_REQUIRED,EntityContext.SHIFT);
        }
        if(!this.date.equals(other.date)) {

            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_OVERLAP_CONFLICT,EntityContext.SHIFT);
        }
        if (!this.dentistId.equals(other.dentistId)) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_DENTIST_REQUIRED,EntityContext.SHIFT);
        }
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    /**
     * RN-SHIFT-009: No puede modificarse si está dentro de 24h previas sin autorización
     */
    public void reschedule(LocalDate newDate, LocalTime newStart, LocalTime newEnd, boolean hasAuthorization) {
        if (newDate == null || newStart == null || newEnd == null) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED,EntityContext.SHIFT);
        }
        if (!newStart.isBefore(newEnd)) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_INVALID_TIME_RANGE, EntityContext.SHIFT);
        }

        LocalDateTime shiftDateTime = LocalDateTime.of(this.date, this.startTime);
        LocalDateTime now = LocalDateTime.now();

        if (shiftDateTime.minus(MODIFICATION_WINDOW).isBefore(now) && !hasAuthorization) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_LATE_MODIFICATION, EntityContext.SHIFT);
        }

        this.date = newDate;
        this.startTime = newStart;
        this.endTime = newEnd;
    }

    /**
     * RN-SHIFT-007: Cancelación requiere motivo obligatorio
     */
    public void cancel(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_CANCELLATION_REQUIRES_REASON, EntityContext.SHIFT);
        }
        this.status = this.status.cancel();
        this.cancellationReason = reason;
    }

    // QUERIES

    /**
     * Verifica si el turno cubre un momento específico
     */
    public boolean covers(LocalDateTime dateTime) {
        if (dateTime == null || !status.isActive()) return false;
        if (!dateTime.toLocalDate().equals(this.date)) return false;
        LocalTime time = dateTime.toLocalTime();
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    /**
     * Verifica si el turno cubre un intervalo completo
     */
    public boolean coversInterval(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !status.isActive()) return false;
        if (!start.toLocalDate().equals(this.date) || !end.toLocalDate().equals(this.date)) {
            return false;
        }
        return !start.toLocalTime().isBefore(this.startTime) &&
                !end.toLocalTime().isAfter(this.endTime);
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public int getDurationInHours() {
        return (int) getDuration().toHours();
    }

    public boolean isActive() {
        return status.isActive();
    }

    public boolean isInPast() {
        LocalDateTime shiftEnd = LocalDateTime.of(date, endTime);
        return shiftEnd.isBefore(LocalDateTime.now());
    }

    public boolean isWithinNext24Hours() {
        LocalDateTime shiftStart = LocalDateTime.of(date, startTime);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(24);
        return shiftStart.isAfter(now) && !shiftStart.isAfter(limit);
    }

   // GETTERS

    public ShiftId getId() {
        return id;
    }

    public DentistId getDentistId() {
        return dentistId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public ShiftType getType() {
        return type;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shift)) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
