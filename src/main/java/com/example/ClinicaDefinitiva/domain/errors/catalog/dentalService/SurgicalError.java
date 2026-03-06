package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum SurgicalError implements ErrorCatalog {

    ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH(
            "RN-SURGICAL-001","error.surgical.anesthesia.complexity",
            "Si requiere anestesia, el nivel de complejidad debe ser al menos MEDIUM",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_SURGICAL_INVALID_COMPLEXITY(
            "RN-SURGICAL-002","error.surgical.complexity.invalid",
            "El nivel de complejidad debe ser: LOW, MEDIUM, HIGH o CRITICAL",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS(
            "RN-SURGICAL-003","error.surgical.critical.requirements",
            "Cirugías CRITICAL deben requerir anestesia y quirófano",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_SURGICAL_TYPE_TOO_SHORT(
            "RN-SURGICAL-004","error.surgical.type.short",
            "El tipo de cirugía debe tener al menos 3 caracteres si se especifica",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH(
            "RN-SURGICAL-005","error.surgical.operatingroom.complexity",
            "Cirugías que requieren quirófano deben tener complejidad al menos MEDIUM",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    SurgicalError(String code, String messageKey, String defaultMessage,
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