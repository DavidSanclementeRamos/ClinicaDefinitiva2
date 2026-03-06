package com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum PediatricError implements ErrorCatalog {

    ERR_PEDIATRIC_INVALID_AGE_RANGE(
            "RN-PEDIATRIC-001","error.pediatric.age.invalid",
            "El rango de edad debe especificar edades pediátricas válidas (0-18 años)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PEDIATRIC_MATERIALS_TOO_SHORT(
            "RN-PEDIATRIC-002","error.pediatric.materials.short",
            "Materiales pediátricos deben describirse adecuadamente (mínimo 5 caracteres)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    PediatricError(String code, String messageKey, String defaultMessage,
                   HttpStatus suggestedHttpStatus, ErrorSeverity severity) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        this.suggestedHttpStatus = suggestedHttpStatus;
        this.severity = severity;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
    @Override public HttpStatus getSuggestedHttpStatus() { return suggestedHttpStatus; }
    @Override public ErrorSeverity getSeverity() { return severity; }
}