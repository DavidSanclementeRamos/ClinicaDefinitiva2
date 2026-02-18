package com.example.ClinicaDefinitiva.domain.errors.catalog.operations;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ShiftError implements ErrorCatalog {
    // ========== APLICADAS ==========

    ERR_SHIFT_INVALID_TIME_RANGE(// // PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-001",
            "error.shift.invalidTimeRange",
            "La hora de inicio debe ser anterior a la hora de fin"
    ),

   /** ERR_SHIFT_PROFESSIONAL_INACTIVE(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-002",
            "error.shift.professionalInactive",
            "No puede crearse si el profesional está inactivo"
    ),*/

    // RN-SHIFT-002: ELIMINADA (2024-12-29)
    // Motivo: Delegada a Dentist.ensureEditable()
    // Original: "No puede crearse si el profesional está inactivo"
    // Ver: ADR-24 para detalles completos

    ERR_SHIFT_OVERLAP_CONFLICT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-003",
            "error.shift.overlapConflict",
            "No puede solaparse con otro turno del mismo profesional"
    ),

    /**ERR_SHIFT_CANNOT_EDIT(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-004",
            "error.shift.cannotEdit",
            "No puede editarse si tiene tareas asignadas o está dentro de 24h"
    ),

    ERR_SHIFT_HAS_ACTIVE_TASKS(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-005",
            "error.shift.hasActiveTasks",
            "No puede cancelarse si tiene tareas activas"
    ),

    ERR_SHIFT_INVALID_LOCATION(// POSPONER, NO HAY SEDE, PROYECTO EXPERIMENTAL
            "RN-SHIFT-006",
            "error.shift.invalidLocation",
            "Debe estar asociado a una sede válida"
    ),*/

    // RN-SHIFT-004: ELIMINADA (2024-12-29)
    // Motivo: Redundancia con validaciones de Appointment
    // Original: "No puede editarse si tiene tareas asignadas o está dentro de 24h"
    // Nota: Las "tareas" son citas (Appointment). Validación debe hacerse en Domain Service
    // Ver: ADR-24

    // RN-SHIFT-005: ELIMINADA (2024-12-29)
    // Motivo: Redundancia con Appointment (idéntico a RN-SHIFT-004)
    // Original: "No puede cancelarse si tiene tareas activas"
    // Ver: ADR-24

    // RN-SHIFT-006: POSPUESTA (2024-12-29)
    // Motivo: Proyecto experimental sin múltiples sedes
    // Original: "Debe estar asociado a una sede válida"
    // Estado: Pendiente para v2.0 cuando exista contexto de múltiples sedes
    // Ver: ADR-24

    ERR_SHIFT_CANCELLATION_REQUIRES_REASON(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-007",
            "error.shift.cancellationRequiresReason",
            "La cancelación requiere motivo obligatorio"
    ),

    ERR_SHIFT_ZERO_DURATION(// BIEN
            "RN-SHIFT-008",
            "error.shift.zeroDuration",
            "No puede tener duración negativa o cero"
    ),

    ERR_SHIFT_LATE_MODIFICATION(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-009",
            "error.shift.lateModification",
            "No puede modificarse si está dentro de 24h previas sin autorización"
    ),
    ERR_SHIFT_DENTIST_REQUIRED(
            "RN-SHIFT-010",
            "error.shift.dentistRequired",
            "Debe especificarse un DentistId válido para crear un turno"
    ),

    ERR_SHIFT_DATE_REQUIRED(
            "RN-SHIFT-011",
            "error.shift.dateRequired",
            "Debe especificarse una fecha válida para crear un turno"
    ),

    ERR_SHIFT_TIME_REQUIRED(
            "RN-SHIFT-012",
            "error.shift.timeRequired",
            "Debe especificarse hora de inicio y fin para crear un turno"
    ),

    ERR_SHIFT_TYPE_REQUIRED(
            "RN-SHIFT-013",
            "error.shift.typeRequired",
            "Debe especificarse un tipo de turno válido"
    ),
    ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED(
            "RN-SHIFT-014",
            "error.shift.rescheduleParametersRequired",
            "Debe especificarse nueva fecha y horas de inicio y fin para reprogramar el turno"
    ),
    ERR_SHIFT_OVERLAP_TARGET_REQUIRED(
            "RN-SHIFT-015",
            "error.shift.overlapTargetRequired",
            "Debe especificarse un turno válido para evaluar solapamiento"
    ),
    ERR_SHIFT_NO_ACTIVE_COVERAGE(
            "RN-SHIFT-016",
            "error.shift.noActiveCoverage",
            "El dentista no tiene turno activo en ese horario"
    ),
 /**
  * RN-SHIFT-017: Excluir bloque de tiempo (almuerzo, reunión, pausa)
  */
 ERR_SHIFT_BLOCK_TIME_REQUIRED(
         "RN-SHIFT-017",
         "error.shift.block.timeRequired",
         "El bloque de tiempo debe tener hora de inicio y fin"
 ),

 /**
  * RN-SHIFT-018: Razón obligatoria para el bloque
  */
 ERR_SHIFT_BLOCK_REASON_REQUIRED(
         "RN-SHIFT-018",
         "error.shift.block.reasonRequired",
         "Debe especificarse la razón del bloque de tiempo"
 ),

 /**
  * RN-SHIFT-019: Rango de tiempo inválido

 ERR_SHIFT_INVALID_TIME_RANGE(
         "RN-SHIFT-019",
         "error.shift.invalidTimeRange",
         "La hora de inicio debe ser anterior a la hora de fin"
 ),*/

 /**
  * RN-SHIFT-020: Bloque fuera del turno
  */
 ERR_SHIFT_BLOCK_OUTSIDE_SHIFT(
         "RN-SHIFT-020",
         "error.shift.block.outsideShift",
         "El bloque de tiempo debe estar dentro del turno asignado"
 ),

 /**
  * RN-SHIFT-021: Bloque solapado
  */
 ERR_SHIFT_BLOCK_OVERLAP(
         "RN-SHIFT-021",
         "error.shift.block.overlap",
         "El bloque de tiempo se solapa con otro bloque ya excluido"
 ),

 /**
  * RN-SHIFT-022: Cita fuera del turno
  */
 ERR_SHIFT_APPOINTMENT_OUTSIDE_SHIFT(
         "RN-SHIFT-022",
         "error.shift.appointment.outsideShift",
         "La cita no está cubierta por el turno activo"
 );

 /**
  * RN-SHIFT-016: Validación de turno activo (ya existente)

 ERR_SHIFT_NO_ACTIVE_COVERAGE(
         "RN-SHIFT-016",
         "error.shift.noActiveCoverage",
         "El dentista no tiene turno activo en ese horario"
 );*/

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
