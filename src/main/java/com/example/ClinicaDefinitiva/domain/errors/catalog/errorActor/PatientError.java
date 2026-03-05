package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PatientError implements ErrorCatalog {

    
    ERR_PATIENT_ACTIVE_SERVICES(
            "RN-PATIENT-001",
            "error.patient.deactivate.services",
            "No puede desactivarse si tiene citas activas o tratamientos en curso"
    ),
    ERR_PATIENT_TIME_CONFLICT(
            "RN-PATIENT-002",
            "error.patient.schedule.conflict",
            "El paciente ya tiene una cita agendada en este horario"
    ),

    

    ERR_PATIENT_INVALID_AGE(
            "RN-PATIENT-003",
            "error.patient.age.invalid",
            "La edad del paciente debe estar en el rango válido (0-120 años)"
    ),

 
    ERR_PATIENT_MINOR_REQUIRES_GUARDIAN(
            "RN-PATIENT-004",
            "error.patient.guardian.required",
            "Los pacientes menores de edad deben tener un responsable legal vinculado"
    ),

    // RN-PATIENT-009: POSPUESTA -> APLICADA v1.0 ✅
    ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY(
            "RN-PATIENT-005",
            "error.patient.birthdate.immutable",
            "No se puede modificar la fecha de nacimiento si el paciente tiene historial de citas"
    ),

    ERR_PATIENT_DEACTIVATION_REQUIRES_REASON(
            "RN-PATIENT-006",
            "error.patient.deactivate.reason",
            "La desactivación requiere motivo obligatorio"
    ),

    // ========== NUEVOS ==========
    ERR_PATIENT_NO_SHIFT_ASSIGNED(
            "RN-PATIENT-007",
            "error.patient.shift.missing",
            "El paciente no tiene un turno asignado"
    ),
    ERR_PATIENT_SHIFT_NOT_AVAILABLE(
            "RN-PATIENT-008",
            "error.patient.shift.unavailable",
            "El horario solicitado no está dentro del turno asignado al paciente"
    ),
    ERR_PATIENT_ACTIVE_TREATMENT(
            "RN-PATIENT-009",
            "error.patient.activeTreatment",
            "El paciente tiene tratamientos activos y no puede ser desactivado" ),
    ERR_PATIENT_CONTRACT_INVALID(
            "RN-PATIENT-010",
            "error.patient.contractInvalid",
            "El contrato asignado al paciente no está activo o se encuentra vencido"
    );


    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    PatientError(String code, String messageKey, String defaultMessage) {
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
