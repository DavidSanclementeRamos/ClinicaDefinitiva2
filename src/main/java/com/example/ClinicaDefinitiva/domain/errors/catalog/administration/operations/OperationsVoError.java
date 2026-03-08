package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.operations;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum OperationsVoError implements ErrorCatalog {

    ERR_SHIFT_ID_REQUIRED("RN-SHIFT-VO-001","error.shift.idRequired","El valor de ShiftId no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SHIFT_INVALID_COMPLETION("RN-SHIFT-VO-002","error.shift.invalid.completion","No se puede completar el turno en el estado actual: {currentStatus}",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_SHIFT_INVALID_CANCELLATION("RN-SHIFT-VO-003","error.shift.invalid.cancellation","No se puede cancelar el turno en el estado actual: {currentStatus}",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_EXCLUDED_BLOCK_INVALID_RANGE("RN-SHIFT-VO-004","error.excludedBlock.invalidRange","El rango de tiempo del bloque excluido es inválido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SHIFT_STATUS_NULL("RN-SHIFT-VO-005","error.shift.statusNull","El estado del turno no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    // CORREGIDO: código duplicado RN-SHIFT-005 → RN-SHIFT-VO-006
    ERR_EXCLUDED_BLOCK_NULL_TIME("RN-SHIFT-VO-006","error.excludedBlock.nullTime","El bloque excluido debe tener hora de inicio y fin definidas",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    OperationsVoError(String code, String messageKey, String defaultMessage,
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