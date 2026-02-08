package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.Operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.administration.Operations.Shift;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain Service: Orquesta operaciones entre agregados
 *
 * SIMPLIFICADO: Ya no valida Availability (eliminada)
 */
public class AppointmentSchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final ShiftRepository shiftRepository;
    private final ScheduleQueryService scheduleQueryService;

    public AppointmentSchedulingService(
            AppointmentRepository appointmentRepository,
            ShiftRepository shiftRepository,
            ScheduleQueryService scheduleQueryService) {
        this.appointmentRepository = appointmentRepository;
        this.shiftRepository = shiftRepository;
        this.scheduleQueryService = scheduleQueryService;
    }

    /**
     * Agenda una nueva cita
     *
     * Flujo simplificado:
     * 1. Dentist.canScheduleBetween() → Valida WorkingHours
     * 2. Patient.canScheduleBetween() → Valida estado
     * 3. Shift.canAccommodateAppointment() → ÚNICA validación temporal
     * 4. Detectar conflictos con lock
     */
    public Appointment scheduleAppointment(
            Dentist dentist,
            Patient patient,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason,
            ProvidedService service
             ) {


        Shift shift = ensureShiftCoverage(dentist.getDentistId(), start, end);


        ensureNoConflicts(dentist.getDentistId(), patient.getPatientId(), start, end);

        // 4. Crear cita
        Appointment appointment = buildAppointment(
                dentist.getDentistId(),
                patient.getPatientId(),
                service.getId(),
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
            LocalDateTime newEnd
             ) {

        if (!original.getStatus().isEditable()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_NOT_EDITABLE, EntityContext.APPOINTMENT);
        }

        if (original.isWithinNext24Hours(LocalDateTime.now())) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_MINIMUM_RESCHEDULE_NOTICE, EntityContext.APPOINTMENT);
        }


        ensureShiftCoverage(dentist.getDentistId(), newStart, newEnd);

        ensureNoConflictsExcluding(
                original.getId(),
                dentist.getDentistId(),
                patient.getPatientId(),
                newStart,
                newEnd
        );

        original.markAsRescheduled();
        appointmentRepository.save(original);

        Appointment newAppointment = buildAppointment(
                dentist.getDentistId(),
                patient.getPatientId(),
                original.getServiceId(),
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
    public void cancelAppointment(Appointment appointment, String reason) {
        appointment.cancel(reason);
        appointmentRepository.save(appointment);
    }


    /**
     * Coordina con Shift: Verifica que dentista tenga turno activo
     * Y que el turno pueda acomodar la cita (sin bloques excluidos)
     */
    private Shift ensureShiftCoverage(
            com.example.ClinicaDefinitiva.domain.actor.vo.DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end) {

        if (!start.toLocalDate().equals(end.toLocalDate())) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS, EntityContext.APPOINTMENT);
        }

        List<Shift> shifts = shiftRepository.findActiveByDentistAndDate(
                dentistId,
                start.toLocalDate()
        );

        return shifts.stream()
                .filter(shift -> shift.canAccommodateAppointment(start, end))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException(
                        ShiftError.ERR_SHIFT_NO_ACTIVE_COVERAGE, EntityContext.SHIFT));
    }

    /**
     * RN-APPT-004, RN-APPT-009: Coordina conflictos entre Appointments CON LOCK
     */
    private void ensureNoConflicts(
            com.example.ClinicaDefinitiva.domain.actor.vo.DentistId dentistId,
            com.example.ClinicaDefinitiva.domain.actor.vo.PatientId patientId,
            LocalDateTime start,
            LocalDateTime end) {

        List<Appointment> dentistConflicts = appointmentRepository
                .findConflictingForDentist(dentistId, start, end, true);

        if (!dentistConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_DENTIST_TIME_CONFLICT, EntityContext.APPOINTMENT);
        }

        List<Appointment> patientConflicts = appointmentRepository
                .findConflictingForPatient(patientId, start, end, true);

        if (!patientConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_PATIENT_TIME_CONFLICT, EntityContext.APPOINTMENT);
        }
    }

    /**
     * Igual que ensureNoConflicts pero excluyendo una cita específica
     */
    private void ensureNoConflictsExcluding(
            AppointmentId excludeId,
            com.example.ClinicaDefinitiva.domain.actor.vo.DentistId dentistId,
            com.example.ClinicaDefinitiva.domain.actor.vo.PatientId patientId,
            LocalDateTime start,
            LocalDateTime end) {

        List<Appointment> dentistConflicts = appointmentRepository
                .findConflictingForDentist(dentistId, start, end, true);

        dentistConflicts.removeIf(a -> a.getId().equals(excludeId));

        if (!dentistConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_DENTIST_TIME_CONFLICT, EntityContext.APPOINTMENT);
        }

        List<Appointment> patientConflicts = appointmentRepository
                .findConflictingForPatient(patientId, start, end, true);

        patientConflicts.removeIf(a -> a.getId().equals(excludeId));

        if (!patientConflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_PATIENT_TIME_CONFLICT, EntityContext.APPOINTMENT);
        }
    }

    /**
     * Construye Appointment usando Builder CORREGIDO
     */
    private Appointment buildAppointment(
            DentistId dentistId,
            PatientId patientId,
            ServiceId serviceId,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason) {

        ServiceDuration duration = ServiceDuration.between(start, end);

        return new Appointment.Builder()
                //.withId(AppointmentId())
                .withDentistId(dentistId)
                .withPatientId(patientId)
                .withServiceId(serviceId)
                .withStart(start)
                .withEnd(end)
                .withAppointmentType(type)
                .withReason(reason)
                .withServiceDuration(duration)
                .build();
    }
}