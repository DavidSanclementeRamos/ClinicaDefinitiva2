package com.example.ClinicaDefinitiva.domain.errors.catalog.operations;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ShiftError implements ErrorCatalog {
    // ========== APLICADAS ==========

    // si
    ERR_SHIFT_INVALID_TIME_RANGE(// // PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-001",
            "error.shift.invalidTimeRange",
            "La hora de inicio debe ser anterior a la hora de fin"
    ),

 

    // si
    ERR_SHIFT_ZERO_DURATION(// BIEN
            "RN-SHIFT-002",
            "error.shift.zeroDuration",
            "No puede tener duración negativa o cero"
    ),

    // si
    ERR_SHIFT_LATE_MODIFICATION(// PUEDE QUE SE ELIMINE, PUEDE ESTAR DUPLICADA
            "RN-SHIFT-003",
            "error.shift.lateModification",
            "No puede modificarse si está dentro de 24h previas sin autorización"
    ),
    
 // si
    ERR_SHIFT_DATE_REQUIRED(
            "RN-SHIFT-004",
            "error.shift.dateRequired",
            "Debe especificarse una fecha válida para crear un turno"
    ),

     // si
    ERR_SHIFT_TIME_REQUIRED(
            "RN-SHIFT-005",
            "error.shift.timeRequired",
            "Debe especificarse hora de inicio y fin para crear un turno"
    ),

   
    // si
    ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED(
            "RN-SHIFT-006",
            "error.shift.rescheduleParametersRequired",
            "Debe especificarse nueva fecha y horas de inicio y fin para reprogramar el turno"
    ),
    
    // si
    ERR_SHIFT_OVERLAP_TARGET_REQUIRED(
            "RN-SHIFT-007",
            "error.shift.overlapTargetRequired",
            "Debe especificarse un turno válido para evaluar solapamiento"
    ),
    
    // si
    ERR_SHIFT_NO_ACTIVE_COVERAGE(
            "RN-SHIFT-008",
            "error.shift.noActiveCoverage",
            "El dentista no tiene turno activo en ese horario"
    ),
   
 // si
 ERR_SHIFT_BLOCK_TIME_REQUIRED(
         "RN-SHIFT-009",
         "error.shift.block.timeRequired",
         "El bloque de tiempo debe tener hora de inicio y fin"
 ),

  // si
 ERR_SHIFT_BLOCK_REASON_REQUIRED(
         "RN-SHIFT-010",
         "error.shift.block.reasonRequired",
         "Debe especificarse la razón del bloque de tiempo"
 ),

 // si
 ERR_SHIFT_BLOCK_OUTSIDE_SHIFT(
         "RN-SHIFT-011",
         "error.shift.block.outsideShift",
         "El bloque de tiempo debe estar dentro del turno asignado"
 ),

 
 // si
 ERR_SHIFT_BLOCK_OVERLAP(
         "RN-SHIFT-011",
         "error.shift.block.overlap",
         "El bloque de tiempo se solapa con otro bloque ya excluido"
 ),

 

 // si
ERR_SHIFT_INVALID_COMPLETION(
    "RN-SHIFT-012",
    "error.shift.invalidCompletion",
    "El turno no puede marcarse como completado en un estado inválido"
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
