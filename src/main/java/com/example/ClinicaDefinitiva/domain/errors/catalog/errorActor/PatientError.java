package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PatientError implements ErrorCatalog {

    // RN-PATIENT-001: ELIMINADA (2024-12)
    // Motivo: Validaciones ocurren en ValueObjects
    // Original: "Debe tener nombre, documento y fecha de nacimiento válida"
    // Reemplazo: ValueObjectError.ERR_FULLNAME_BLANK, ERR_DOCUMENT_INVALID_FORMAT, ERR_BIRTHDATE_FUTURE

    ERR_PATIENT_ACTIVE_SERVICES(
            "RN-PATIENT-002",
            "error.patient.deactivate.services",
            "No puede desactivarse si tiene citas activas o tratamientos en curso"
    ),
    ERR_PATIENT_TIME_CONFLICT(
            "RN-PATIENT-003",
            "error.patient.schedule.conflict",
            "El paciente ya tiene una cita agendada en este horario"
    ),

    // RN-PATIENT-004: ELIMINADA (2024-12)
    // Motivo: Delegada a UserAccessError.ERR_USER_INACTIVE
    // Original: "Solo puede editarse si está activo"

    // RN-PATIENT-005: ELIMINADA (Catálogo actualizado)
    // Motivo: Validación ocurre en ValueObjects
    // Original: "Debe registrar al menos un medio de contacto válido"

    ERR_PATIENT_INVALID_AGE(
            "RN-PATIENT-006",
            "error.patient.age.invalid",
            "La edad del paciente debe estar en el rango válido (0-120 años)"
    ),

    // RN-PATIENT-007: ELIMINADA (2024-12)
    // Motivo: Validación ocurre en ValueObject DateOfBirth
    // Original: "Fecha de nacimiento no puede ser futura"
    // Reemplazo: ValueObjectError.ERR_BIRTHDATE_FUTURE

    ERR_PATIENT_MINOR_REQUIRES_GUARDIAN(
            "RN-PATIENT-008",
            "error.patient.guardian.required",
            "Los pacientes menores de edad deben tener un responsable legal vinculado"
    ),

    // RN-PATIENT-009: POSPUESTA -> APLICADA v1.0 ✅
    ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY(
            "RN-PATIENT-009",
            "error.patient.birthdate.immutable",
            "No se puede modificar la fecha de nacimiento si el paciente tiene historial de citas"
    ),

    ERR_PATIENT_DEACTIVATION_REQUIRES_REASON(
            "RN-PATIENT-010",
            "error.patient.deactivate.reason",
            "La desactivación requiere motivo obligatorio"
    ),

    // ========== NUEVOS ==========
    ERR_PATIENT_NO_SHIFT_ASSIGNED(
            "RN-PATIENT-011",
            "error.patient.shift.missing",
            "El paciente no tiene un turno asignado"
    ),
    ERR_PATIENT_SHIFT_NOT_AVAILABLE(
            "RN-PATIENT-012",
            "error.patient.shift.unavailable",
            "El horario solicitado no está dentro del turno asignado al paciente"
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
