package com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService;


import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum OrthodonticError implements ErrorCatalog {

    ERR_ORTHODONTIC_MISSING_APPLIANCE(
            "RN-ORTHODONTIC-001","error.orthodontic.appliance.missing",
            "El tipo de aparato es obligatorio y no puede estar en blanco",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ORTHODONTIC_INVALID_DURATION(
            "RN-ORTHODONTIC-002","error.orthodontic.duration.invalid",
            "La duración del tratamiento debe estar entre 6 y 48 meses",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_ORTHODONTIC_INVALID_APPLIANCE(
            "RN-ORTHODONTIC-003","error.orthodontic.appliance.invalid",
            "El tipo de aparato debe ser reconocido por el sistema",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ORTHODONTIC_NEGATIVE_DURATION(
            "RN-ORTHODONTIC-004","error.orthodontic.duration.negative",
            "La duración del tratamiento debe ser positiva",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    OrthodonticError(String code, String messageKey, String defaultMessage,
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
