package com.example.ClinicaDefinitiva.domain.errors.catalog.schedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 * Catálogo de errores del agregado Appointment
 * Ver: ADR-25 para historial completo de catálogos eliminados
 */
public enum AppointmentError implements ErrorCatalog {

    // RN-APPT-001: ELIMINADA (2025-12-28)
    // Motivo: Delegada a Dentist.canScheduleBetween(user, start, end)
    // Original: "No puede crearse si el odontólogo está inactivo"
    // Ver: ADR-24 para detalles completos

    // YA NO SE USA 
    ERR_APPT_OUTSIDE_AVAILABILITY(
            "RN-APPT-002",
            "error.appointment.outsideAvailability",
            "No puede agendarse fuera del horario de disponibilidad"
    ),

    // NO SE USA
    ERR_APPT_MISSING_REQUIRED_FIELDS(// SI, AUNQUE DEBERIA SEPARARSE PARA MEJOR SEMANTICA
            "RN-APPT-003",
            "error.appointment.missingFields",
            "Debe tener paciente y odontólogo válidos"
    ),

    // SI
    ERR_APPT_DENTIST_TIME_CONFLICT(
            "RN-APPT-004",
            "error.appointment.dentistConflict",
            "No puede haber dos citas en el mismo horario para el mismo odontólogo"
    ),

    // si
    ERR_APPT_INCOMPLETE_COMPLETION(
            "RN-APPT-005",
            "error.appointment.incompleteCompletion",
            "Solo puede finalizarse si tiene duración real y notas clínicas"
    ),

    // SI
    ERR_APPT_NOT_EDITABLE(
            "RN-APPT-006",
            "error.appointment.notEditable",
            "Solo puede editarse si está en estado SCHEDULED o CONFIRMED"
    ),

    // si
    ERR_APPT_LATE_CANCELLATION(
            "RN-APPT-007",
            "error.appointment.lateCancellation",
            "No puede cancelarse dentro de las 24h previas"
    ),
// ESTA DUBLICA
    ERR_APPT_CANCELLATION_REQUIRES_REASON(
            "RN-APPT-008",
            "error.appointment.cancellationRequiresReason",
            "La cancelación requiere motivo obligatorio"
    ),

    // SI
    ERR_APPT_PATIENT_TIME_CONFLICT(// SI
            "RN-APPT-009",
            "error.appointment.patientConflict",
            "No puede haber dos citas en el mismo horario para el mismo paciente"
    ),

    // YA NO SE USA
    ERR_APPT_PAST_DATE(// SI
            "RN-APPT-010",
            "error.appointment.pastDate",
            "La fecha/hora de la cita no puede estar en el pasado"
    ),

    // si
    ERR_APPT_MISSING_REASON(// SI
            "RN-APPT-011",
            "error.appointment.missingReason",
            "Motivo clínico es obligatorio"
    ),
    
    // SI
    ERR_APPT_MINIMUM_RESCHEDULE_NOTICE(
            "RN-APPT-012",
            "error.appointment.minimumRescheduleNotice",
            "No se puede reagendar con menos de 24 horas de anticipación"
    ),
    
    // SI
    ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS(
            "RN-APPT-013",
            "error.appointment.cannotSpanMultipleDays",
            "La cita no puede cruzar días"
    ),

    // ELIMINAR
    ERR_APPT_REQUIRED_ID(
            "RN-APPT-014",
            "error.appointment.requiredId",
            "El identificador de la cita es obligatorio"
    ),

    // ELIMMINAR
    ERR_APPT_REQUIRED_DENTIST(
            "RN-APPT-015",
            "error.appointment.requiredDentist",
            "El dentista es obligatorio"
    ),

    // ELIMINAR
    ERR_APPT_REQUIRED_PATIENT(
            "RN-APPT-016",
            "error.appointment.requiredPatient",
            "El paciente es obligatorio"
    ),
// ELIMINARR
    ERR_APPT_REQUIRED_SERVICE(
            "RN-APPT-017",
            "error.appointment.requiredService",
            "El servicio es obligatorio"
    ),

    ERR_APPT_REQUIRED_START(
            "RN-APPT-018",
            "error.appointment.requiredStart",
            "La hora de inicio es obligatoria"
    ),

    ERR_APPT_REQUIRED_END(
            "RN-APPT-019",
            "error.appointment.requiredEnd",
            "La hora de fin es obligatoria"
    ),

    ERR_APPT_REQUIRED_REASON(
            "RN-APPT-020",
            "error.appointment.requiredReason",
            "El motivo de la cita es obligatorio"
    ),

    ERR_APPT_REQUIRED_TYPE(
            "RN-APPT-021",
            "error.appointment.requiredType",
            "El tipo de cita es obligatorio"
    ),

    ERR_APPT_INVALID_DATE_RANGE(
            "RN-APPT-022",
            "error.appointment.invalidDateRange",
            "La hora de inicio debe ser anterior a la hora de fin"
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
