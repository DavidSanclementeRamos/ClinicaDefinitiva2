package com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments;


import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum TreatmentError implements ErrorCatalog {
    ERR_TREATMENT_FUTURE_START_DATE(
        "RN-TREATMENT-001",
        "error.treatment.futureStartDate",
        "La fecha de inicio del tratamiento no puede ser futura"
),

ERR_TREATMENT_INVALID_END_DATE(
        "RN-TREATMENT-002",
        "error.treatment.invalidEndDate",
        "La fecha de fin esperada debe ser posterior a la fecha de inicio"
),

ERR_TREATMENT_NOT_ACTIVE(
        "RN-TREATMENT-003",
        "error.treatment.notActive",
        "El tratamiento debe estar activo para esta operación"
),

ERR_TREATMENT_INVALID_COMPLETION_DATE(
        "RN-TREATMENT-004",
        "error.treatment.invalidCompletionDate",
        "La fecha de finalización no puede ser anterior a la fecha de inicio"
),

ERR_TREATMENT_CANCELLATION_REASON_REQUIRED(
        "RN-TREATMENT-005",
        "error.treatment.cancellationReasonRequired",
        "La cancelación requiere un motivo detallado (mínimo 10 caracteres)"
),

ERR_TREATMENT_PHASES_REQUIRED(
        "RN-TREATMENT-006",
        "error.treatment.phasesRequired",
        "El tratamiento debe tener al menos una fase definida"
);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    TreatmentError(String code, String messageKey, String defaultMessage) {
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

