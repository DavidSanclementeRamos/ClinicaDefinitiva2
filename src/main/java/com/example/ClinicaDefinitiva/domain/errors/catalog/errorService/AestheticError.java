package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AestheticError implements ErrorCatalog {

    // RN-AESTHETIC-001
    ERR_AESTHETIC_MISSING_TYPE(
            "RN-AESTHETIC-001",
            "error.aesthetic.type.missing",
            "El tipo de procedimiento estético es obligatorio"
    ),

    // RN-AESTHETIC-002
    ERR_AESTHETIC_INVALID_TYPE(
            "RN-AESTHETIC-002",
            "error.aesthetic.type.invalid",
            "El tipo de procedimiento debe ser reconocido por el sistema"
    ),

    // RN-AESTHETIC-003
    ERR_AESTHETIC_TYPE_TOO_SHORT(
            "RN-AESTHETIC-003",
            "error.aesthetic.type.short",
            "El tipo de procedimiento debe tener al menos 3 caracteres"
    ),

    // RN-AESTHETIC-004
    ERR_AESTHETIC_RESULT_TOO_SHORT(
            "RN-AESTHETIC-004",
            "error.aesthetic.result.short",
            "El resultado esperado debe tener al menos 10 caracteres si se especifica"
    ),

    // Warnings
    WARN_AESTHETIC_UNREALISTIC_WHITENING(
            "RN-AESTHETIC-005",
            "warn.aesthetic.whitening.unrealistic",
            "Blanqueamiento no debe prometer más de 10 tonos de aclaración"
    ),

    WARN_AESTHETIC_VENEER_IRREVERSIBILITY(
            "RN-AESTHETIC-006",
            "warn.aesthetic.veneer.irreversible",
            "Carillas deben mencionar irreversibilidad en resultado esperado"
    ),

    WARN_AESTHETIC_MISSING_MATERIAL(
            "RN-AESTHETIC-007",
            "warn.aesthetic.material.missing",
            "Procedimientos con porcelana deberían especificar material"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    AestheticError(String code, String messageKey, String defaultMessage) {
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
