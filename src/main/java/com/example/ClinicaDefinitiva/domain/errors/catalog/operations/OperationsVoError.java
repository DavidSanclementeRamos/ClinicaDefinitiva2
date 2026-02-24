package com.example.ClinicaDefinitiva.domain.errors.catalog.operations;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum OperationsVoError implements ErrorCatalog {

    ERR_SHIFT_ID_REQUIRED(
            "RN-SHIFT-001",
            "error.shift.idRequired",
            "El valor de ShiftId no puede ser nulo"
    ),

    ERR_SHIFT_ID_BLANK(
            "RN-SHIFT-002",
            "error.shift.idBlank",
            "El valor de ShiftId no puede estar vacío"
    ),
    ERR_SHIFT_INVALID_COMPLETION(
            "RN-SHIFT-003",
            "error.shift.invalid.completion",
            "No se puede completar el turno en el estado actual: {currentStatus}"
    ),
    ERR_SHIFT_INVALID_CANCELLATION(
            "RN-SHIFT-004",
            "error.shift.invalid.cancellation",
            "No se puede cancelar el turno en el estado actual: {currentStatus}"
    ),
    ERR_EXCLUDED_BLOCK_NULL_TIME("","",""),
    ERR_EXCLUDED_BLOCK_INVALID_RANGE("","","");
    

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    OperationsVoError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
