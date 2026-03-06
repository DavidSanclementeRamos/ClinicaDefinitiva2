package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum DentistError implements ErrorCatalog {

    ERR_DENTIST_AGE_INSUFFICIENT(
            "RN-DENTIST-001","error.dentist.age",
            "El odontólogo debe tener al menos 25 años",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_DENTIST_MISSING_AVAILABILITY(
            "RN-DENTIST-002","error.dentist.availability.missing",
            "El odontólogo debe registrar disponibilidad inicial (mínimo 40 horas semanales)",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_DENTIST_ACTIVE_APPOINTMENTS(
            "RN-DENTIST-003","error.dentist.deactivate.appointments",
            "No puede desactivarse si tiene citas activas en las próximas 24 horas",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_DENTIST_TIME_CONFLICT(
            "RN-DENTIST-004","error.dentist.schedule.conflict",
            "El odontólogo ya tiene una cita agendada en este horario",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_DENTIST_NOT_AVAILABLE(
            "RN-DENTIST-005","error.dentist.not.available",
            "El odontólogo no está disponible para agendar en este momento",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_DENTIST_INVALID_SPECIALTY(
            "RN-DENTIST-006","error.dentist.specialty.invalid",
            "La especialidad proporcionada no es reconocida",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_DENTIST_EMPTY_AVAILABILITY(
            "RN-DENTIST-007","error.dentist.availability.empty",
            "La disponibilidad del odontólogo no puede quedar vacía",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_DENTIST_OUT_OF_WORKING_HOURS(
            "RN-DENTIST-008","error.dentist.working.hours",
            "El horario solicitado está fuera de las horas laborales declaradas",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_DENTIST_INVALID_VACATION_RANGE(
            "RN-DENTIST-009","error.dentist.vacation.range",
            "El rango de vacaciones solicitado es inválido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_DENTIST_VACATION_CONFLICT(
            "RN-DENTIST-010","error.dentist.vacation.conflict",
            "Hay citas agendadas que entran en conflicto con el período de vacaciones",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS(
            "RN-DENTIST-011","error.dentist.reschedule.hours",
            "La reagendación está fuera de las horas laborales del odontólogo",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    DentistError(String code, String messageKey, String defaultMessage,
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