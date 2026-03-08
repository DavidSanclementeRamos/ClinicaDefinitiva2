package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.operations.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ProvidedServiceError;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;

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
    private final ProvidedServiceRepository serviceRepository;

    public AppointmentSchedulingService(AppointmentRepository appointmentRepository, ShiftRepository shiftRepository, ScheduleQueryService scheduleQueryService, ProvidedServiceRepository serviceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.shiftRepository = shiftRepository;
        this.scheduleQueryService = scheduleQueryService;
        this.serviceRepository = serviceRepository;
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
            DentistId dentistId,
            PatientId patientId,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason,
            ServiceId serviceId
             ) {


        Shift shift = ensureShiftCoverage(dentistId, start, end);


        ensureNoConflicts(dentistId, patientId, start, end, serviceId
);

        // 4. Crear cita
        Appointment appointment = buildAppointment(
                dentistId,
                patientId,
                serviceId,
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
            DentistId dentistId,
            PatientId patientId,
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


        ensureShiftCoverage(dentistId, newStart, newEnd);

        ensureNoConflictsExcluding(
                original.getId(),
                dentistId,
                patientId,
                newStart,
                newEnd
        );

        Appointment newAppointment = buildAppointment(
                dentistId,
                patientId,
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
            DentistId dentistId,
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
     * RN-APPT-004, RN-APPT-009: Coordina conflictos entre Appointments CON LOCK y servicios activos
     */
    private void ensureNoConflicts(
            DentistId dentistId,
            PatientId patientId,
            LocalDateTime start,
            LocalDateTime end,
            ServiceId serviceId) {

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
            ProvidedService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        ProvidedServiceError.ERR_SERVICE_NOT_FOUND,
                        EntityContext.DENTAL_SERVICE
                ));

        if (!service.isActive()) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_INACTIVE,
                    EntityContext.DENTAL_SERVICE
            );
        }

    }

    /**
     * Igual que ensureNoConflicts pero excluyendo una cita específica
     */
    private void ensureNoConflictsExcluding(
            AppointmentId excludeId,
            DentistId dentistId,
            PatientId patientId,
            LocalDateTime start,
            LocalDateTime end
            ) {

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
                .withDentistId(dentistId)
                .withPatientId(patientId)
                .withServiceId(serviceId)
                .withStart(start)
                .withEnd(end)
                .withAppointmentType(type)
                .withReason(reason)
                .build();
    }
}