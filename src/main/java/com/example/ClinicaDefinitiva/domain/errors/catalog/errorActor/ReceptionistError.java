package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ReceptionistError implements ErrorCatalog {

    ERR_RECEPTIONIST_DENTIST_INACTIVE(
            "RN-RECEPTIONIST-001",
            "error.receptionist.dentist.inactive",
            "No puede confirmar citas para odontólogos inactivos"
    ),
    ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT(
            "RN-RECEPTIONIST-002",
            "error.receptionist.appointment.duplicate",
            "No puede agendar citas duplicadas para el mismo paciente en el mismo horario"
    ),
    ERR_RECEPTIONIST_LATE_CANCELLATION(
            "RN-RECEPTIONIST-003",
            "error.receptionist.cancel.late",
            "Solo puede cancelar citas si no están dentro de las 24h previas"
    ),

    // RN-RECEPTIONIST-004: POSPUESTA (Proyecto experimental sin sedes)
    ERR_RECEPTIONIST_INVALID_LOCATION(
            "RN-RECEPTIONIST-004",
            "error.receptionist.location.invalid",
            "El recepcionista debe estar asociado a una sede válida"
    ),

    // RN-RECEPTIONIST-005: ELIMINADA (2024-12)
    // Motivo: Delegada a UserAccessError.ERR_USER_INACTIVE
    // Original: "Solo puede editarse si está activo"

    // RN-RECEPTIONIST-006: POSPUESTA (Módulo de tareas pendiente)
    ERR_RECEPTIONIST_HAS_PENDING_TASKS(
            "RN-RECEPTIONIST-006",
            "error.receptionist.deactivate.tasks",
            "No puede desactivarse si tiene tareas pendientes"
    ),

    ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS(
            "RN-RECEPTIONIST-007",
            "error.receptionist.location.assignments",
            "No puede modificar sede si tiene citas asignadas en curso"
    ),

    ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER(// sera eliminado
            "RN-RECEPTIONIST-008",
            "error.receptionist.create.user",
            "Solo se pueden registrar recepcionistas con usuarios activos"
    ),

    // RN-RECEPTIONIST-009: Regla aplicada, catálogo actualizado
    // Original: ERR_RECEPTIONIST_MISSING_CONTACT
    // Ahora validado en ValueObjects

    ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON(
            "RN-RECEPTIONIST-010",
            "error.receptionist.deactivate.reason",
            "La desactivación requiere motivo obligatorio"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ReceptionistError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() { return code; }
    @Override
    public String getMessageKey() { return messageKey; }
    @Override
    public String getDefaultMessage() { return defaultMessage; }
}
