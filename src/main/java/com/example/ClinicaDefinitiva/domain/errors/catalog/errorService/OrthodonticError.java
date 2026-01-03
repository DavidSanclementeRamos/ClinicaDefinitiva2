package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;


import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum OrthodonticError implements ErrorCatalog {

    // RN-ORTHODONTIC-001
    ERR_ORTHODONTIC_MISSING_APPLIANCE(
            "RN-ORTHODONTIC-001",
            "error.orthodontic.appliance.missing",
            "El tipo de aparato es obligatorio y no puede estar en blanco"
    ),

    // RN-ORTHODONTIC-002
    ERR_ORTHODONTIC_INVALID_DURATION(
            "RN-ORTHODONTIC-002",
            "error.orthodontic.duration.invalid",
            "La duración del tratamiento debe estar entre 6 y 48 meses"
    ),

    // RN-ORTHODONTIC-003
    ERR_ORTHODONTIC_INVALID_APPLIANCE(
            "RN-ORTHODONTIC-003",
            "error.orthodontic.appliance.invalid",
            "El tipo de aparato debe ser reconocido por el sistema"
    ),

    // RN-ORTHODONTIC-004
    ERR_ORTHODONTIC_NEGATIVE_DURATION(
            "RN-ORTHODONTIC-004",
            "error.orthodontic.duration.negative",
            "La duración del tratamiento debe ser positiva"
    ),

    // Warnings (opcionales, para logs)
    WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION(
            "RN-ORTHODONTIC-005",
            "warn.orthodontic.aligner.duration",
            "Alineadores transparentes típicamente duran 12-24 meses"
    ),

    WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION(
            "RN-ORTHODONTIC-006",
            "warn.orthodontic.lingual.duration",
            "Brackets linguales deben tener duración mínima de 18 meses"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    OrthodonticError(String code, String messageKey, String defaultMessage) {
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
