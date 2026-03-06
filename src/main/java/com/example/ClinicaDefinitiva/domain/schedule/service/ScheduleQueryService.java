package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Query Service: Centraliza consultas complejas sobre la agenda.
 *
 * Problema corregido: Pageable.ofSize(100) hardcodeado.
 *
 * Solución aplicada con dos estrategias según el tipo de consulta:
 *
 * A) Consultas de existencia (hasXxx):
 *    Usan métodos existsBy del repositorio que ejecutan COUNT(*) LIMIT 1.
 *    Son O(1) independientemente del volumen de citas. No hay riesgo de
 *    truncar silenciosamente resultados porque solo necesitan saber "hay alguna".
 *
 * B) Consultas de lista (findXxx):
 *    Usan MAX_APPOINTMENTS_PER_QUERY como techo explícito y documentado.
 *    Si el repositorio devuelve exactamente ese número se loguea una advertencia
 *    (señal de que el límite fue alcanzado). En producción estos métodos deberían
 *    exponer un Pageable al caller; esto es el siguiente paso de madurez.
 */
@Service
public class ScheduleQueryService {

    /**
     * Límite máximo de citas que se cargan en memoria en una sola consulta.
     *
     * 500 es un techo razonable para validaciones de negocio (desactivación,
     * conflictos de agenda). Si se alcanza, hay un problema de modelado más
     * profundo (ej. el dentista lleva años sin depurar citas) que debe resolverse
     * en el repositorio con consultas COUNT, no cargando más elementos en memoria.
     */
    private static final int MAX_APPOINTMENTS_PER_QUERY = 500;

    private final AppointmentRepository appointmentRepository;

    public ScheduleQueryService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Consultas de EXISTENCIA — estrategia A: COUNT en repositorio
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Verifica si el dentista tiene citas activas en las próximas N horas.
     * Usado en: DentistDeactivationValidator (RN-DENTIST-003).
     */
    public boolean hasAppointmentsWithinHours(DentistId dentistId, int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(hours);
        return appointmentRepository.existsScheduledByDentistBetween(dentistId, now, limit);
    }

    /**
     * Verifica si el paciente tiene citas activas en los próximos N días.
     * Usado en: PatientDeactivationValidator (RN-PATIENT-001).
     */
    public boolean hasAppointmentsWithin(PatientId patientId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);
        return appointmentRepository.existsScheduledByPatientBetween(patientId, now, limit);
    }

    /**
     * Verifica si el dentista tiene citas activas en los próximos N días.
     */
    public boolean hasAppointmentsWithin(DentistId dentistId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);
        return appointmentRepository.existsScheduledByDentistBetween(dentistId, now, limit);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Consultas de LISTA — estrategia B: límite explícito y documentado
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Retorna citas programadas del dentista en las próximas N horas.
     * Usado en: DentistIncapacityService (cancelación masiva).
     */
    public List<Appointment> findAppointmentsWithinHours(DentistId dentistId, int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(hours);
        Pageable page = PageRequest.of(0, MAX_APPOINTMENTS_PER_QUERY);

        List<Appointment> results = appointmentRepository
                .findByDentistBetween(dentistId, now, limit, page)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();

        warnIfLimitReached(results.size(), "findAppointmentsWithinHours", dentistId.toString());
        return results;
    }

    /**
     * Retorna citas programadas del dentista en los próximos N días.
     */
    public List<Appointment> findAppointmentsWithin(DentistId dentistId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);
        Pageable page = PageRequest.of(0, MAX_APPOINTMENTS_PER_QUERY);

        List<Appointment> results = appointmentRepository
                .findByDentistBetween(dentistId, now, limit, page)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();

        warnIfLimitReached(results.size(), "findAppointmentsWithin(days)", dentistId.toString());
        return results;
    }

    /**
     * Retorna citas programadas del paciente en los próximos N días.
     */
    public List<Appointment> findAppointmentsWithin(PatientId patientId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);
        Pageable page = PageRequest.of(0, MAX_APPOINTMENTS_PER_QUERY);

        List<Appointment> results = appointmentRepository
                .findByPatientBetween(patientId, now, limit, page)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();

        warnIfLimitReached(results.size(), "findAppointmentsWithin(PatientId)", patientId.toString());
        return results;
    }

    /**
     * Retorna todas las citas del dentista en una fecha concreta.
     */
    public List<Appointment> findAppointmentsOn(DentistId dentistId, LocalDate date) {
        Pageable page = PageRequest.of(0, MAX_APPOINTMENTS_PER_QUERY);

        List<Appointment> results = appointmentRepository
                .findByDentistAndDate(dentistId, date, page)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();

        warnIfLimitReached(results.size(), "findAppointmentsOn", dentistId.toString());
        return results;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Métricas
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Tiempo total ocupado por citas activas en una fecha específica.
     */
    public Duration getTotalOccupiedTime(DentistId dentistId, LocalDate date) {
        return findAppointmentsOn(dentistId, date)
                .stream()
                .map(a -> Duration.between(a.getStart(), a.getEnd()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Número de citas activas del dentista.
     * Usa COUNT directo en repositorio para evitar carga innecesaria en memoria.
     */
    public long getActiveAppointmentCount(DentistId dentistId) {
        return appointmentRepository.countScheduledByDentist(dentistId);
    }

    /**
     * Verifica conflictos de horario para un dentista en un intervalo dado.
     */
    public boolean hasConflictingAppointments(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end) {
        return appointmentRepository.existsScheduledByDentistBetween(dentistId, start, end);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Utilitario interno
    // ──────────────────────────────────────────────────────────────────────────

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(ScheduleQueryService.class);

    /**
     * Advierte si el resultado alcanzó el límite de la consulta.
     * Indica que puede haber más registros que no fueron cargados.
     */
    private void warnIfLimitReached(int resultSize, String method, String entityId) {
        if (resultSize >= MAX_APPOINTMENTS_PER_QUERY) {
            log.warn("ScheduleQueryService.{}: resultado alcanzó el límite de {} registros " +
                     "para entityId={}. Puede haber citas adicionales no consideradas. " +
                     "Evaluar consulta COUNT o paginación en el caller.",
                     method, MAX_APPOINTMENTS_PER_QUERY, entityId);
        }
    }
}
