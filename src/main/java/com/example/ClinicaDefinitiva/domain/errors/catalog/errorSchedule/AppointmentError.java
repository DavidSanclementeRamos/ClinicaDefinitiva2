package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AppointmentError implements ErrorCatalog {


    // ========== APLICADAS ==========

    ERR_APPT_DENTIST_INACTIVE(// ELIMINAR
            "RN-APPT-001",
            "error.appointment.dentist.inactive",
            "No puede crearse si el odontólogo está inactivo"
    ),

    ERR_APPT_OUTSIDE_AVAILABILITY(// SI
            "RN-APPT-002",
            "error.appointment.outsideAvailability",
            "No puede agendarse fuera del horario de disponibilidad"
    ),

    ERR_APPT_MISSING_REQUIRED_FIELDS(// SI, AUNQUE DEBERIA SEPARARSE PARA MEJOR SEMANTICA
            "RN-APPT-003",
            "error.appointment.missingFields",
            "Debe tener paciente y odontólogo válidos"
    ),

    ERR_APPT_DENTIST_TIME_CONFLICT(// SI, AUN NO ESTA APLICADA
            "RN-APPT-004",
            "error.appointment.dentistConflict",
            "No puede haber dos citas en el mismo horario para el mismo odontólogo"
    ),

    ERR_APPT_INCOMPLETE_COMPLETION(// POSPONER, NO LA ENTIENDO
            "RN-APPT-005",
            "error.appointment.incompleteCompletion",
            "Solo puede finalizarse si tiene duración real y notas clínicas"
    ),

    ERR_APPT_NOT_EDITABLE(// SI
            "RN-APPT-006",
            "error.appointment.notEditable",
            "Solo puede editarse si está en estado SCHEDULED o CONFIRMED"
    ),

    ERR_APPT_LATE_CANCELLATION(// SI
            "RN-APPT-007",
            "error.appointment.lateCancellation",
            "No puede cancelarse dentro de las 24h previas"
    ),

    ERR_APPT_CANCELLATION_REQUIRES_REASON(// SI
            "RN-APPT-008",
            "error.appointment.cancellationRequiresReason",
            "La cancelación requiere motivo obligatorio"
    ),

    ERR_APPT_PATIENT_TIME_CONFLICT(// SI
            "RN-APPT-009",
            "error.appointment.patientConflict",
            "No puede haber dos citas en el mismo horario para el mismo paciente"
    ),

    ERR_APPT_PAST_DATE(// SI
            "RN-APPT-010",
            "error.appointment.pastDate",
            "La fecha/hora de la cita no puede estar en el pasado"
    ),

    ERR_APPT_MISSING_REASON(// SI
            "RN-APPT-011",
            "error.appointment.missingReason",
            "Motivo clínico es obligatorio"
    ),
    ERR_APPT_MINIMUM_RESCHEDULE_NOTICE(
            "RN-APPT-012",
            "error.appointment.minimumRescheduleNotice",
            "No se puede reagendar con menos de 24 horas de anticipación"
    ),
    ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS(
            "RN-APPT-013",
            "error.appointment.cannotSpanMultipleDays",
            "La cita no puede cruzar días"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    AppointmentError(String code, String messageKey, String defaultMessage) {
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
