package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.portsOutput.ScheduleRepository.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.ScheduleRepository.AvailabilityRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.ScheduleRepository.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Availability;
import com.example.ClinicaDefinitiva.domain.administration.Operations.Shift;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain Service: Orquesta operaciones entre agregados
 * NO VALIDA - Solo delega y coordina
 */
public class AppointmentSchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ShiftRepository shiftRepository;

    public AppointmentSchedulingService(
            AppointmentRepository appointmentRepository,
            AvailabilityRepository availabilityRepository,
            ShiftRepository shiftRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
        this.shiftRepository = shiftRepository;
    }

    /**
     * Agenda una nueva cita
     *
     * Reglas aplicadas:
     * - Dentist y Patient validan TODO lo interno vía canScheduleBetween()
     * - Service solo coordina: Shift, Availability, Conflictos con Lock
     */
    public Appointment scheduleAppointment(
            Dentist dentist,
            Patient patient,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason,
            ProvidedService service,
            UserIdentity user,
            Shift shift,
            Availability availability) {

        dentist.canScheduleBetween(user, start, end);
        patient.canScheduleBetween(user, start, end);

        ensureShiftCoverage(dentist.getDentistId(), start, end);

        ensureAvailabilityCoverage(dentist.getDentistId(), start, end);

        ensureNoConflicts(dentist.getDentistId(), patient.getPatientId(), start, end);

        Appointment appointment = buildAppointment(
                dentist.getDentistId(),
                patient.getPatientId(),
                start,
                end,
                type,
                reason
        );

        return appointmentRepository.save(appointment);
    }

    /**
     * Reagenda una cita existente
     */
    public Appointment rescheduleAppointment(
            Appointment original,
            Dentist dentist,
            Patient patient,
            LocalDateTime newStart,
            LocalDateTime newEnd,
            UserIdentity user) {

        if (!original.getStatus().isEditable()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_NOT_EDITABLE, EntityContext.APPOINTMENT);
        }

        if (original.isWithinNext24Hours(LocalDateTime.now())) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_MINIMUM_RESCHEDULE_NOTICE,EntityContext.APPOINTMENT);
        }


        dentist.validateReschedule(user, newStart, newEnd);
        patient.canScheduleBetween(user,newStart,newEnd);

        ensureShiftCoverage(dentist.getDentistId(), newStart, newEnd);
        ensureAvailabilityCoverage(dentist.getDentistId(), newStart, newEnd);

        ensureNoConflictsExcluding(
                original.getId(),
                dentist.getDentistId(),
                patient.getPatientId(),
                newStart,
                newEnd
        );

        original.markAsRescheduled();
        appointmentRepository.save(original);

        //  CREAR: Nueva cita
        Appointment newAppointment = buildAppointment(
                dentist.getDentistId(),
                patient.getPatientId(),
                newStart,
                newEnd,
                original.getAppointmentType(),
                original.getReason() + " [Reprogramada]"
        );

        return appointmentRepository.save(newAppointment);
    }

    /**
     * Cancela una cita
     */
    public void cancelAppointment(Appointment appointment,String reason) {
        //  DELEGAR: Appointment valida sus reglas de cancelación
        appointment.cancel(reason);  // Ya valida RN-APPT-007 (24h) y RN-APPT-008 (motivo)

        appointmentRepository.save(appointment);
    }

    // MÉTODOS PRIVADOS DE COORDINACIÓN

    /**
     * Coordina con Shift: Verifica que dentista tenga turno activo
     */
    public void ensureShiftCoverage(DentistId dentistId , LocalDateTime start, LocalDateTime end) {

        if (!start.toLocalDate().equals(end.toLocalDate())) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS,EntityContext.APPOINTMENT);
        }

        List<Shift> shifts = shiftRepository.findActiveByDentistAndDate(
                dentistId,
                start.toLocalDate()
        );

        boolean covered = shifts.stream()
                .anyMatch(shift -> shift.coversInterval(start, end));

        if (!covered) {
            throw new BusinessRuleViolationException(ShiftError.ERR_SHIFT_NO_ACTIVE_COVERAGE,EntityContext.SHIFT);
        }
    }

    /**
     * RN-APPT-002: Coordina con Availability
     */
    private void ensureAvailabilityCoverage(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end) {

        List<Availability> availabilities = availabilityRepository.findByDentistAndDay(
                dentistId,
                start.getDayOfWeek()
        );

        boolean covered = availabilities.stream()
                .anyMatch(av -> av.coversInterval(
                        start.getDayOfWeek(),
                        start.toLocalTime(),
                        end.toLocalTime()
                ));

        if (!covered) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_OUTSIDE_AVAILABILITY,EntityContext.APPOINTMENT);
        }
    }

    /**
     * RN-APPT-004, RN-APPT-009: Coordina conflictos entre Appointments CON LOCK
     */
    private void ensureNoConflicts(
            com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId dentistId,
            com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId patientId,
            LocalDateTime start,
            LocalDateTime end) {

        // Conflictos con dentista
        List<Appointment> dentistConflicts = appointmentRepository
                .findConflictingForDentist(dentistId, start, end, true); // withLock=true

        if (!dentistConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_DENTIST_TIME_CONFLICT,EntityContext.APPOINTMENT);
        }

        // Conflictos con paciente
        List<Appointment> patientConflicts = appointmentRepository
                .findConflictingForPatient(patientId, start, end, true); // withLock=true

        if (!patientConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_PATIENT_TIME_CONFLICT,EntityContext.APPOINTMENT);
        }
    }

    /**
     * Igual que ensureNoConflicts pero excluyendo una cita específica
     * (usado en reagendamiento)
     */
    private void ensureNoConflictsExcluding(
            AppointmentId excludeId,
            com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId dentistId,
            com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId patientId,
            LocalDateTime start,
            LocalDateTime end) {

        List<Appointment> dentistConflicts = appointmentRepository
                .findConflictingForDentist(dentistId, start, end, true);

        dentistConflicts.removeIf(a -> a.getId().equals(excludeId));

        if (!dentistConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_DENTIST_TIME_CONFLICT,EntityContext.APPOINTMENT);
        }

        List<Appointment> patientConflicts = appointmentRepository
                .findConflictingForPatient(patientId, start, end, true);

        patientConflicts.removeIf(a -> a.getId().equals(excludeId));

        if (!patientConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_PATIENT_TIME_CONFLICT,EntityContext.APPOINTMENT);
        }
    }

    /**
     * Construye Appointment usando Builder
     * El Builder ya valida:
     * - RN-APPT-010: start no en el pasado
     * - RN-APPT-011: motivo obligatorio
     * - RN-APPT-003: actores válidos (no null)
     * - start < end
     */
    private Appointment buildAppointment(
            DentistId dentistId,
            PatientId patientId,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason){

            ServiceDuration duration = ServiceDuration.between(start, end);

            return new Appointment.Builder()
                    .withDentistId(dentistId)
                    .withPatientId(patientId)
                    .withStart(start)
                    .withEnd(end)
                    .withAppointmentType(type)
                    .withReason(reason)
                    .withServiceDuration(duration)
                    .build();
    }

}