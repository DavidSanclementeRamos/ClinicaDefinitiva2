package com.example.ClinicaDefinitiva.domain.errors.catalog.schedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

/**
 * Catálogo de errores del agregado Appointment
 * Ver: ADR-25 para historial completo de catálogos eliminados
 */
public enum AppointmentError implements ErrorCatalog {

    ERR_APPT_OUTSIDE_AVAILABILITY(
            "RN-APPT-001","error.appointment.outsideAvailability",
            "No puede agendarse fuera del horario de disponibilidad",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_APPT_DENTIST_TIME_CONFLICT(
            "RN-APPT-002","error.appointment.dentistConflict",
            "No puede haber dos citas en el mismo horario para el mismo odontólogo",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_APPT_NOT_EDITABLE(
            "RN-APPT-003","error.appointment.notEditable",
            "Solo puede editarse si está en estado SCHEDULED",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_APPT_LATE_CANCELLATION(
            "RN-APPT-004","error.appointment.lateCancellation",
            "No puede cancelarse dentro de las 24h previas",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_APPT_PATIENT_TIME_CONFLICT(
            "RN-APPT-005","error.appointment.patientConflict",
            "No puede haber dos citas en el mismo horario para el mismo paciente",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_APPT_MISSING_REASON(
            "RN-APPT-006","error.appointment.missingReason",
            "Motivo clínico es obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_APPT_MINIMUM_RESCHEDULE_NOTICE(
            "RN-APPT-007","error.appointment.minimumRescheduleNotice",
            "No se puede reagendar con menos de 24 horas de anticipación",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS(
            "RN-APPT-008","error.appointment.cannotSpanMultipleDays",
            "La cita no puede cruzar días",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    AppointmentError(String code, String messageKey, String defaultMessage,
                     HttpStatus suggestedHttpStatus, ErrorSeverity severity) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        this.suggestedHttpStatus = suggestedHttpStatus;
        this.severity = severity;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
    @Override public HttpStatus getSuggestedHttpStatus() { return suggestedHttpStatus; }
    @Override public ErrorSeverity getSeverity() { return severity; }
}