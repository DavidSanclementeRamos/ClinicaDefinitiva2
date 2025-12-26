package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AvailabilityError implements ErrorCatalog {
    // ========== APLICADAS ==========

    ERR_AVAIL_INVALID_TIME_RANGE(// BIEN
            "RN-AVAIL-001",
            "error.availability.invalidTimeRange",
            "La hora de inicio debe ser anterior a la hora de fin"
    ),

    ERR_AVAIL_ZERO_DURATION(// BIEN
            "RN-AVAIL-002",
            "error.availability.zeroDuration",
            "No puede crearse disponibilidad con duración negativa o cero"
    ),

    ERR_AVAIL_HAS_ACTIVE_APPOINTMENTS(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-003",
            "error.availability.hasActiveAppointments",
            "No puede modificarse si tiene citas agendadas dentro del bloque"
    ),

    ERR_AVAIL_OVERLAP_CONFLICT(// BIEN
            "RN-AVAIL-004",
            "error.availability.overlapConflict",
            "No puede haber dos bloques que se solapen para el mismo profesional"
    ),

    ERR_AVAIL_CANNOT_DELETE_WITH_APPOINTMENTS(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-005",
            "error.availability.cannotDeleteWithAppointments",
            "No puede eliminarse si tiene citas activas asociadas"
    ),

    ERR_AVAIL_DENTIST_INACTIVE(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-006",
            "error.availability.dentistInactive",
            "Debe estar asociada a un profesional activo"
    ),

    ERR_AVAIL_CANNOT_EDIT_INACTIVE_DENTIST(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-007",
            "error.availability.cannotEditInactiveDentist",
            "Solo puede editarse si el profesional está activo"
    ),

    ERR_AVAIL_DEACTIVATION_REQUIRES_REASON(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-008",
            "error.availability.deactivationRequiresReason",
            "La desactivación requiere motivo obligatorio"
    ),

    ERR_AVAIL_EXTENSION_CONFLICT(// BIEN
            "RN-AVAIL-009",
            "error.availability.extensionConflict",
            "No puede extenderse sobre otro bloque ya registrado"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    AvailabilityError(String code, String messageKey, String defaultMessage) {
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
