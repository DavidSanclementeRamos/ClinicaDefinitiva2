package com.example.ClinicaDefinitiva.domain.administration.operations.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ExcludedBlock;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.operations.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * Agregado: Shift (Turno Operativo)
 *
 * ÚNICA FUENTE DE VERDAD para validar si un dentista puede atender en un momento dado.
 *
 * Responsabilidades:
 * - Representar presencia física del profesional en la clínica
 * - Modelar restricciones temporales (almuerzo, reuniones) mediante ExcludedBlocks
 * - Validar si puede acomodar una cita en un intervalo específico
 *
 * Diferencia clave con WorkingHours:
 * - WorkingHours: Contrato laboral recurrente (ej. "Lunes 8-17h")
 * - Shift: Presencia operativa específica (ej. "15-Feb-2026 9-18h")
 */
public class Shift {

    private final ShiftId id;
    private final DentistId dentistId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftType type;
    private ShiftStatus status;
    private String cancellationReason;
    private List<ExcludedBlock> excludedBlocks = new ArrayList<>();
    private Long version;
    private static final Duration MODIFICATION_WINDOW = Duration.ofHours(24);


    private Shift(ShiftId id, DentistId dentistId, LocalDate date,
                  LocalTime startTime, LocalTime endTime, ShiftType type) {
        this.id = id;
        this.dentistId = dentistId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = ShiftStatus.of(ShiftStatus.Status.ACTIVE);
        this.excludedBlocks = new ArrayList<>();
    }


    /**
     * RN-SHIFT-001: La hora de inicio debe ser anterior a la hora de fin
     * RN-SHIFT-008: No puede tener duración negativa o cero
     */
    public static Shift create( DentistId dentistId, LocalDate date,
                               LocalTime startTime, LocalTime endTime, ShiftType type) {
        
        if (date == null) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_DATE_REQUIRED, EntityContext.SHIFT);
        }
        if (startTime == null || endTime == null) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_TIME_REQUIRED, EntityContext.SHIFT);
        }
       
        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_INVALID_TIME_RANGE, EntityContext.SHIFT);
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.isZero() || duration.isNegative()) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_ZERO_DURATION, EntityContext.SHIFT);
        }

        return new Shift(null,dentistId, date, startTime, endTime, type);
    }


    /**
     * RN-SHIFT-010: Excluir bloque de tiempo (almuerzo, reunión, pausa)
     */
    public void excludeBlock(LocalTime blockStart, LocalTime blockEnd, String reason) {
        if (blockStart == null || blockEnd == null) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_BLOCK_TIME_REQUIRED, EntityContext.SHIFT);
        }
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_BLOCK_REASON_REQUIRED, EntityContext.SHIFT);
        }
        if (!blockStart.isBefore(blockEnd)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_INVALID_TIME_RANGE, EntityContext.SHIFT);
        }

        // Validar que el bloque esté dentro del turno
        if (blockStart.isBefore(this.startTime) || blockEnd.isAfter(this.endTime)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_BLOCK_OUTSIDE_SHIFT, EntityContext.SHIFT);
        }

        ExcludedBlock newBlock = new ExcludedBlock(blockStart, blockEnd, reason);

        // Validar que no se solape con otros bloques excluidos
        for (ExcludedBlock existing : excludedBlocks) {
            if (existing.overlapsWith(newBlock)) {
                throw new BusinessRuleViolationException(
                        ShiftError.ERR_SHIFT_BLOCK_OVERLAP, EntityContext.SHIFT);
            }
        }

        excludedBlocks.add(newBlock);
    }

    /**
     * RN-SHIFT-011: Verificar si puede acomodar una cita
     *
     * Esta es la ÚNICA validación temporal necesaria.
     * Reemplaza la necesidad de Availability.
     */
    public boolean canAccommodateAppointment(LocalDateTime start, LocalDateTime end) {
        if (!status.isActive()) {
            return false;
        }


        if (!coversInterval(start, end)) {
            return false;
        }

        for (ExcludedBlock block : excludedBlocks) {
            if (block.overlapsWith(start.toLocalTime(), end.toLocalTime())) {
                return false;
            }
        }

        return true;
    }


    /**
     * RN-SHIFT-003: No puede solaparse con otro turno del mismo profesional
     */
    public boolean overlapsWith(Shift other) {
        if (other == null) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_OVERLAP_TARGET_REQUIRED, EntityContext.SHIFT);
        }
        if (!this.date.equals(other.date)) {
            return false;
        }
        if (!this.dentistId.equals(other.dentistId)) {
            return false;
        }
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    /**
     * RN-SHIFT-009: No puede modificarse si está dentro de 24h previas sin autorización
     */
    public void reschedule(LocalDate newDate, LocalTime newStart, LocalTime newEnd,
                           boolean hasAuthorization) {
        if (newDate == null || newStart == null || newEnd == null) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED, EntityContext.SHIFT);
        }
        if (!newStart.isBefore(newEnd)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_INVALID_TIME_RANGE, EntityContext.SHIFT);
        }

        LocalDateTime shiftDateTime = LocalDateTime.of(this.date, this.startTime);
        LocalDateTime now = LocalDateTime.now();

        if (shiftDateTime.minus(MODIFICATION_WINDOW).isBefore(now) && !hasAuthorization) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_LATE_MODIFICATION, EntityContext.SHIFT);
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
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_CANCELLATION_REQUIRES_REASON, EntityContext.SHIFT);
        }
        this.status = this.status.cancel();
        this.cancellationReason = reason;
    }
    
        /**
     * RN-SHIFT-012: Finalización del turno requiere validación de transición
     */
    public void complete() {
        if (!status.canTransitionTo(ShiftStatus.Status.COMPLETED)) {
            throw new BusinessRuleViolationException(
                ShiftError.ERR_SHIFT_INVALID_COMPLETION,
                EntityContext.SHIFT
            );
        }
        this.status = this.status.complete();
    }




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

    /**
     * Calcula tiempo neto disponible (turno - bloques excluidos)
     */
    public Duration getNetAvailableTime() {
        Duration total = getDuration();
        Duration excluded = excludedBlocks.stream()
                .map(ExcludedBlock::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        return total.minus(excluded);
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


    public ShiftId getId() { return id; }
    public DentistId getDentistId() { return dentistId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public ShiftType getType() { return type; }
    public ShiftStatus getStatus() { return status; }
    public String getCancellationReason() { return cancellationReason; }
    public List<ExcludedBlock> getExcludedBlocks() { return List.copyOf(excludedBlocks); }
    public Long getVersion() { return version; }

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