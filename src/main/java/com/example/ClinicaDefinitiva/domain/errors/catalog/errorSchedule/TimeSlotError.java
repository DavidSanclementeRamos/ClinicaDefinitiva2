package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum TimeSlotError implements ErrorCatalog {

    // ========== APLICADAS ==========

    ERR_TIMESLOT_INVALID_DURATION(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-001",
            "error.timeslot.invalidDuration",
            "La duración debe ser positiva y dentro de límites permitidos"
    ),

    ERR_TIMESLOT_PROFESSIONAL_INACTIVE(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-002",
            "error.timeslot.professionalInactive",
            "No puede crearse si el profesional está inactivo"
    ),

    ERR_TIMESLOT_OVERLAP_CONFLICT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-003",
            "error.timeslot.overlapConflict",
            "No puede solaparse con otro TimeSlot ya asignado"
    ),

    ERR_TIMESLOT_CANNOT_EDIT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-004",
            "error.timeslot.cannotEdit",
            "No puede editarse si tiene cita asignada o está dentro de 24h previas"
    ),

    ERR_TIMESLOT_ALREADY_BOOKED(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-005",
            "error.timeslot.alreadyBooked",
            "No puede tener más de una cita asignada"
    ),

    ERR_TIMESLOT_OUTSIDE_AVAILABILITY(// PUEDE QUE SI O NO
            "RN-TIMESLOT-006",
            "error.timeslot.outsideAvailability",
            "Debe estar contenido dentro de una disponibilidad válida"
    ),

    ERR_TIMESLOT_CANCELLATION_REQUIRES_REASON(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-007",
            "error.timeslot.cancellationRequiresReason",
            "Cancelación requiere motivo obligatorio"
    ),

    ERR_TIMESLOT_HAS_ACTIVE_APPOINTMENT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-TIMESLOT-008",
            "error.timeslot.hasActiveAppointment",
            "No puede cancelarse si tiene cita activa"
    ),

    ERR_TIMESLOT_EXCEEDS_AVAILABILITY(// PUEDE QUE SI O NO
            "RN-TIMESLOT-009",
            "error.timeslot.exceedsAvailability",
            "No puede extenderse fuera de la disponibilidad original"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    TimeSlotError(String code, String messageKey, String defaultMessage) {
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
