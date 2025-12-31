package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 * Catálogo de errores del agregado Availability
 * Ver: ADR-24 para historial completo de catálogos eliminados
 */
public enum AvailabilityError implements ErrorCatalog {


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

   /** ERR_AVAIL_HAS_ACTIVE_APPOINTMENTS(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-003",
            "error.availability.hasActiveAppointments",
            "No puede modificarse si tiene citas agendadas dentro del bloque"
    ),*/
    // RN-AVAIL-003: ELIMINADA (2024-12-29)
    // Motivo: Responsabilidad de Domain Service (AvailabilityManagementService)
    // Original: "No puede modificarse si tiene citas agendadas dentro del bloque"
    // Reemplazo: Validación en Domain Service consultando AppointmentRepository
    // Ver: ADR-24 para detalles completos



    ERR_AVAIL_OVERLAP_CONFLICT(// BIEN
            "RN-AVAIL-004",
            "error.availability.overlapConflict",
            "No puede haber dos bloques que se solapen para el mismo profesional"
    ),

   /** ERR_AVAIL_CANNOT_DELETE_WITH_APPOINTMENTS(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
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
    ),*/

    // RN-AVAIL-005: ELIMINADA (2024-12-29)
    // Motivo: Responsabilidad de Domain Service (idéntico a RN-AVAIL-003)
    // Original: "No puede eliminarse si tiene citas activas asociadas"
    // Reemplazo: Validación en Domain Service
    // Ver: ADR-24

    // RN-AVAIL-006: ELIMINADA (2024-12-29)
    // Motivo: Delegada a Dentist.ensureEditable()
    // Original: "Debe estar asociada a un profesional activo"
    // Ver: ADR-24

    // RN-AVAIL-007: ELIMINADA (2024-12-29)
    // Motivo: Delegada a Dentist.ensureEditable() (idéntico a RN-AVAIL-006)
    // Original: "Solo puede editarse si el profesional está activo"
    // Ver: ADR-24

    ERR_AVAIL_DEACTIVATION_REQUIRES_REASON(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-AVAIL-008",
            "error.availability.deactivationRequiresReason",
            "La desactivación requiere motivo obligatorio"
    ),


    ERR_AVAIL_EXTENSION_CONFLICT(// BIEN
            "RN-AVAIL-009",
            "error.availability.extensionConflict",
            "No puede extenderse sobre otro bloque ya registrado"
    ),
    // Catálogo de errores para Availability

    ERR_AVAIL_DENTIST_REQUIRED(
            "RN-AVAIL-010",
            "error.availability.dentistRequired",
            "Debe especificarse un DentistId válido para crear disponibilidad"
    ),

    ERR_AVAIL_DAY_REQUIRED(
            "RN-AVAIL-011",
            "error.availability.dayRequired",
            "Debe especificarse un día de la semana válido para crear disponibilidad"
    ),

    ERR_AVAIL_TIME_REQUIRED(
            "RN-AVAIL-012",
            "error.availability.timeRequired",
            "Debe especificarse hora de inicio y fin para crear disponibilidad"
    ),
    ERR_AVAIL_INVALID_DEACTIVATION(
            "RN-AVAIL-013",
            "error.availability.invalidDeactivation",
            "No puede desactivarse la disponibilidad en el estado actual"
    ),
    ERR_AVAIL_INVALID_ACTIVATION(
            "RN-AVAIL-014",
            "error.availability.invalidActivation",
            "No puede activarse la disponibilidad en el estado actual"
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
