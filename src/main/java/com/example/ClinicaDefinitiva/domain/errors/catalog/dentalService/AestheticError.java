package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;


import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AestheticError implements ErrorCatalog {

    ERR_AESTHETIC_MISSING_TYPE(
            "RN-AESTHETIC-001","error.aesthetic.type.missing",
            "El tipo de procedimiento estético es obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_AESTHETIC_INVALID_TYPE(
            "RN-AESTHETIC-002","error.aesthetic.type.invalid",
            "El tipo de procedimiento debe ser reconocido por el sistema",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_AESTHETIC_TYPE_TOO_SHORT(
            "RN-AESTHETIC-003","error.aesthetic.type.short",
            "El tipo de procedimiento debe tener al menos 3 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_AESTHETIC_RESULT_TOO_SHORT(
            "RN-AESTHETIC-004","error.aesthetic.result.short",
            "El resultado esperado debe tener al menos 10 caracteres si se especifica",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    AestheticError(String code, String messageKey, String defaultMessage,
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