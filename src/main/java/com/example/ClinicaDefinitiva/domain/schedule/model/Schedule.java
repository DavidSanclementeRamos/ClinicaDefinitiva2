package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.invali.date.AppointmentOutsideAvailabilityException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.AppointmentTimeNotAvailableException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.invali.date.AppointmentInvalidDateRangeException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.nulo.AppointmentEndDateMissingException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.nulo.AppointmentStartDateMissingException;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;
import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Schedule {
    // agenda de agregado de cista y disponibilidad
    private final List<Appointment> appointments;
    private final WeeklyAvailability weeklyAvailability;

    public Schedule(Collection<Appointment> appointments, WeeklyAvailability weeklyAvailability) {
        this.appointments = appointments == null ? new ArrayList<>() : new ArrayList<>(appointments);
        this.weeklyAvailability = weeklyAvailability;
    }

    // Method auxiliar para validar si una cita tiene el status isSchedule para las operaciones requeridas
    public boolean validateStatus(Appointment original){
        if(!original.isScheduled()){
            throw new IllegalArgumentException("El estatus: " + original + " no es requerido para la operation");
        }
        return true;
    }
    // Method auxiliar para validar si la cita que se quiere reagendar pertenece a los actores correctos.
    public boolean validateIdentity(Appointment original, Patient patient, Dentist dentist){
        if (!original.getPatient().equals(patient)) {
            throw new IllegalArgumentException("La cita no pertenece al paciente indicado.");
        }
        if (!original.getDentist().equals(dentist)) {
            throw new IllegalArgumentException("La cita no pertenece al odontólogo indicado.");
        }
        return true;
    }

    // ---------------------------
    // QUERIES SEMÁNTICAS
    // ---------------------------

    /** ### Consulta: findAppointmentsWithinHours(int hours)

     **Agregado**: Schedule
     **Propósito**: Obtener todas las citas programadas que comienzan dentro de las próximas `hours` horas.
     **Uso principal**: Soporte para reglas de negocio como `Dentist.deactivate()`.
     **Te da todas las citas que caen en una ventana de horas desde “ahora”.
     *Útil para políticas de tiempo mínimo (ej. “no reagendar si faltan menos de 24h”).
     **No es una regla de negocio en sí misma**, sino una query que habilita otras reglas.*/
    public List<Appointment> findAppointmentsWithinHours(int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(hours);
        return appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), now, limit))
                .toList();
    }

    /**→ Devuelve todas las citas de un día específico.
     **Útil para reportes, disponibilidad diaria, o validaciones de “máximo N citas por día”. */
    public List<Appointment> findAppointmentsOn(LocalDate date) {
        return appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(a -> a.getStart().toLocalDate().equals(date))
                .toList();
    }

    /**→ Devuelve todas las citas en los próximos N días.
     **Útil para reglas de ventana máxima o bloqueos de desactivación de paciente. */
    public List<Appointment> findAppointmentsWithin(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);
        return appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(a -> a.getStart().isAfter(now) && a.getStart().isBefore(limit))
                .toList();
    }

    public boolean hasAppointmentsWithinHours(int hours) {
        return !findAppointmentsWithinHours(hours).isEmpty();
    }
    public boolean hasAppointmentsWithinHour(int hours) {
        if( !findAppointmentsWithinHours(hours).isEmpty()){

        }
        return true;
    }
    public boolean hasAppointmentsWithin(int days){
        return !findAppointmentsWithin( days).isEmpty();
    }


    // ---------------------------
    // VALIDACIÓN DE DISPONIBILIDAD
    // ---------------------------

    public void validateScheduleBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            throw new AppointmentStartDateMissingException(EntityContext.APPOINTMENT,"La fecha de inicio no puede ser null");
        }
        if (end == null) {
            throw new AppointmentEndDateMissingException(EntityContext.APPOINTMENT, "La fecha de fin no puede ser nula.");
        }
        if (!start.isBefore(end)) {
            throw new AppointmentInvalidDateRangeException(EntityContext.APPOINTMENT,"El rango de fechas es inválido: inicio" + start + " no es anterior a fin" + end);
        }



        boolean coveredBySlot = weeklyAvailability.getSlots().stream()
                .anyMatch(slot -> slot.getDayOfWeek().equals(start.getDayOfWeek())
                        && slot.getDayOfWeek().equals(end.getDayOfWeek())
                        && !start.toLocalTime().isBefore(slot.getInicio())
                        && !end.toLocalTime().isAfter(slot.getFin()));

        if (!coveredBySlot) {
            throw new AppointmentOutsideAvailabilityException(EntityContext.APPOINTMENT, "El intervalo no está cubierto por la disponibilidad declarada:" + start + " y " + end);
        }

        boolean slotFree = appointments.stream()
                .filter(Appointment::isScheduled)
                .noneMatch(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), start, end));

        if (!slotFree) {
            throw new AppointmentTimeNotAvailableException(EntityContext.APPOINTMENT, "El intervalo ya está ocupado por otra cita: " + start+ " y " + end);
        }
    }


    // ---------------------------
    // CÁLCULOS TEMPORALES
    // ---------------------------

    /**→ Suma la duración de todas las citas programadas.
     **Más orientado a métricas de productividad o capacidad, no tanto a validaciones de reagendamiento. */
    public Duration getTotalOccupiedTime() {
        return appointments.stream()
                .filter(Appointment::isScheduled)
                .map(a -> Duration.between(a.getStart(), a.getEnd()))
                .reduce(Duration.ZERO, Duration::plus);
    }
    /**→ Calcula disponibilidad neta en un día.
     **Útil para validaciones de agenda (ej. “no reagendar si no hay disponibilidad ese día”). */
    public Duration getTotalAvailableTime(DayOfWeek day) {
         // suma de slots declarados menos tiempo ocupado en ese día
                Duration totalSlots = weeklyAvailability.getSlots().stream()
                        .filter(slot -> slot.getDayOfWeek().equals(day))
                        .map(slot -> Duration.between(slot.getInicio(), slot.getFin()))
                        .reduce(Duration.ZERO, Duration::plus);

               Duration occupied = appointments.stream()
                        .filter(Appointment::isScheduled)
                        .filter(a -> a.getStart().getDayOfWeek().equals(day))
                        .map(a -> Duration.between(a.getStart(), a.getEnd()))
                        .reduce(Duration.ZERO, Duration::plus);

                return totalSlots.minus(occupied).isNegative() ? Duration.ZERO : totalSlots.minus(occupied);
    }

            // ---------------------------
            // OPERACIONES AUXILIARES
            // ---------------------------

            public List<Appointment> retrieveAndCancelConflictingAppointments(LocalDateTime start, LocalDateTime end) {
                List<Appointment> conflicts = appointments.stream()
                        .filter(Appointment::isScheduled)
                        .filter(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), start, end))
                        .toList();

                conflicts.forEach(Appointment::cancel); // suponiendo que Appointment tiene cancel()
                return conflicts;
           }

    // ---------------------------
    // GETTERS INMUTABLES
    // ---------------------------

    public List<Appointment> getAppointments() { return List.copyOf(appointments); }
    public WeeklyAvailability getWeeklyAvailability() { return weeklyAvailability; }
}






