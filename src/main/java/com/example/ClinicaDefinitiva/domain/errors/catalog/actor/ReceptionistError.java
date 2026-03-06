package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ReceptionistError implements ErrorCatalog {

    ERR_RECEPTIONIST_DENTIST_INACTIVE(
            "RN-RECEPTIONIST-001","error.receptionist.dentist.inactive",
            "No puede confirmar citas para odontólogos inactivos",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT(
            "RN-RECEPTIONIST-002","error.receptionist.appointment.duplicate",
            "No puede agendar citas duplicadas para el mismo paciente en el mismo horario",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_LATE_CANCELLATION(
            "RN-RECEPTIONIST-003","error.receptionist.cancel.late",
            "Solo puede cancelar citas si no están dentro de las 24h previas",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_INVALID_LOCATION(
            "RN-RECEPTIONIST-004","error.receptionist.location.invalid",
            "El recepcionista debe estar asociado a una sede válida",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_HAS_PENDING_TASKS(
            "RN-RECEPTIONIST-005","error.receptionist.deactivate.tasks",
            "No puede desactivarse si tiene tareas pendientes",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS(
            "RN-RECEPTIONIST-006","error.receptionist.location.assignments",
            "No puede modificar sede si tiene citas asignadas en curso",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER(
            "RN-RECEPTIONIST-007","error.receptionist.create.user",
            "Solo se pueden registrar recepcionistas con usuarios activos",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON(
            "RN-RECEPTIONIST-008","error.receptionist.deactivate.reason",
            "La desactivación requiere motivo obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_RECEPTIONIST_ASSIGNED_SHIFTS(
            "RN-RECEPTIONIST-009","error.receptionist.assignedShifts",
            "El recepcionista tiene turnos asignados y no puede ser desactivado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RECEPTIONIST_AGE_INSUFFICIENT(
            "RN-RECEPTIONIST-010","error.receptionist.age.insufficient",
            "La edad del recepcionista es insuficiente para cumplir con los requisitos mínimos",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ReceptionistError(String code, String messageKey, String defaultMessage,
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