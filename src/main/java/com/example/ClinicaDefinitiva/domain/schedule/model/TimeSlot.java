package com.example.ClinicaDefinitiva.domain.schedule.model;


import com.example.ClinicaDefinitiva.domain.schedule.vo.AvailabilityId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * TimeSlot: Bloque horario específico derivado de Availability
 * NO es persistido, se genera dinámicamente para consultas de disponibilidad
 */
public final class TimeSlot {

    private final AvailabilityId availabilityId;  // Referencia al padre
    private final LocalDate date;                  // Fecha específica
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int durationMinutes;

    private TimeSlot(AvailabilityId availabilityId, LocalDate date,
                     LocalTime startTime, LocalTime endTime) {
        this.availabilityId = availabilityId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * Genera slots desde una Availability para una fecha específica
     */
    public static java.util.List<TimeSlot> generateFrom(Availability availability,
                                                        LocalDate targetDate,
                                                        int slotDurationMinutes) {
        if (!availability.getDayOfWeek().equals(targetDate.getDayOfWeek())) {
            return java.util.List.of();
        }

        java.util.List<TimeSlot> slots = new java.util.ArrayList<>();
        LocalTime current = availability.getStartTime();
        LocalTime end = availability.getEndTime();

        while (current.plusMinutes(slotDurationMinutes).isBefore(end) ||
                current.plusMinutes(slotDurationMinutes).equals(end)) {

            LocalTime slotEnd = current.plusMinutes(slotDurationMinutes);
            slots.add(new TimeSlot(availability.getId(), targetDate, current, slotEnd));
            current = slotEnd;
        }

        return slots;
    }

    // QUERIES

    public boolean covers(LocalDateTime dateTime) {
        if (!dateTime.toLocalDate().equals(this.date)) return false;
        LocalTime time = dateTime.toLocalTime();
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    public boolean overlapsWith(TimeSlot other) {
        if (!this.date.equals(other.date)) return false;
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, startTime);
    }

    public LocalDateTime getEndDateTime() {
        return LocalDateTime.of(date, endTime);
    }

    // GETTERS

    public AvailabilityId getAvailabilityId() { return availabilityId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(availabilityId, timeSlot.availabilityId) &&
                Objects.equals(date, timeSlot.date) &&
                Objects.equals(startTime, timeSlot.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availabilityId, date, startTime);
    }
}