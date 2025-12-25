package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ShiftError implements ErrorCatalog {
    // ========== APLICADAS ==========

    ERR_SHIFT_INVALID_TIME_RANGE(
            "RN-SHIFT-001",
            "error.shift.invalidTimeRange",
            "La hora de inicio debe ser anterior a la hora de fin"
    ),

    ERR_SHIFT_PROFESSIONAL_INACTIVE(
            "RN-SHIFT-002",
            "error.shift.professionalInactive",
            "No puede crearse si el profesional está inactivo"
    ),

    ERR_SHIFT_OVERLAP_CONFLICT(
            "RN-SHIFT-003",
            "error.shift.overlapConflict",
            "No puede solaparse con otro turno del mismo profesional"
    ),

    ERR_SHIFT_CANNOT_EDIT(
            "RN-SHIFT-004",
            "error.shift.cannotEdit",
            "No puede editarse si tiene tareas asignadas o está dentro de 24h"
    ),

    ERR_SHIFT_HAS_ACTIVE_TASKS(
            "RN-SHIFT-005",
            "error.shift.hasActiveTasks",
            "No puede cancelarse si tiene tareas activas"
    ),

    ERR_SHIFT_INVALID_LOCATION(
            "RN-SHIFT-006",
            "error.shift.invalidLocation",
            "Debe estar asociado a una sede válida"
    ),

    ERR_SHIFT_CANCELLATION_REQUIRES_REASON(
            "RN-SHIFT-007",
            "error.shift.cancellationRequiresReason",
            "La cancelación requiere motivo obligatorio"
    ),

    ERR_SHIFT_ZERO_DURATION(
            "RN-SHIFT-008",
            "error.shift.zeroDuration",
            "No puede tener duración negativa o cero"
    ),

    ERR_SHIFT_LATE_MODIFICATION(
            "RN-SHIFT-009",
            "error.shift.lateModification",
            "No puede modificarse si está dentro de 24h previas sin autorización"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ShiftError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
