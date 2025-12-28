package com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ShiftError implements ErrorCatalog {
    // ========== APLICADAS ==========

    ERR_SHIFT_INVALID_TIME_RANGE(// // PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-01",
            "error.shift.invalidTimeRange",
            "La hora de inicio debe ser anterior a la hora de fin"
    ),

    ERR_SHIFT_PROFESSIONAL_INACTIVE(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-02",
            "error.shift.professionalInactive",
            "No puede crearse si el profesional está inactivo"
    ),

    ERR_SHIFT_OVERLAP_CONFLICT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-03",
            "error.shift.overlapConflict",
            "No puede solaparse con otro turno del mismo profesional"
    ),

    ERR_SHIFT_CANNOT_EDIT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-04",
            "error.shift.cannotEdit",
            "No puede editarse si tiene tareas asignadas o está dentro de 24h"
    ),

    ERR_SHIFT_HAS_ACTIVE_TASKS(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-05",
            "error.shift.hasActiveTasks",
            "No puede cancelarse si tiene tareas activas"
    ),

    ERR_SHIFT_INVALID_LOCATION(// POSPONER, NO HAY SEDE, PROYECTO EXPERIMENTAL
            "RN-SHIFT-06",
            "error.shift.invalidLocation",
            "Debe estar asociado a una sede válida"
    ),

    ERR_SHIFT_CANCELLATION_REQUIRES_REASON(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-07",
            "error.shift.cancellationRequiresReason",
            "La cancelación requiere motivo obligatorio"
    ),

    ERR_SHIFT_ZERO_DURATION(// BIEN
            "RN-SHIFT-08",
            "error.shift.zeroDuration",
            "No puede tener duración negativa o cero"
    ),

    ERR_SHIFT_LATE_MODIFICATION(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-09",
            "error.shift.lateModification",
            "No puede modificarse si está dentro de 24h previas sin autorización"
    ),
    ERR_SHIFT_DENTIST_REQUIRED(
            "RN-SHIFT-10",
            "error.shift.dentistRequired",
            "Debe especificarse un DentistId válido para crear un turno"
    ),

    ERR_SHIFT_DATE_REQUIRED(
            "RN-SHIFT-11",
            "error.shift.dateRequired",
            "Debe especificarse una fecha válida para crear un turno"
    ),

    ERR_SHIFT_TIME_REQUIRED(
            "RN-SHIFT-12",
            "error.shift.timeRequired",
            "Debe especificarse hora de inicio y fin para crear un turno"
    ),

    ERR_SHIFT_TYPE_REQUIRED(
            "RN-SHIFT-13",
            "error.shift.typeRequired",
            "Debe especificarse un tipo de turno válido"
    ),
    ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED(
            "RN-SHIFT-14",
            "error.shift.rescheduleParametersRequired",
            "Debe especificarse nueva fecha y horas de inicio y fin para reprogramar el turno"
    ),
    ERR_SHIFT_OVERLAP_TARGET_REQUIRED(
            "RN-SHIFT-15",
            "error.shift.overlapTargetRequired",
            "Debe especificarse un turno válido para evaluar solapamiento"
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
