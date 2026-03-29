package com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum OpeningBalanceError implements ErrorCatalog {

    ERR_OPENING_BALANCE_MISSING_DATE(
            "RN-OPENINGBALANCE-001","error.openingBalance.missingDate",
            "La fecha es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_OPENING_BALANCE_NOT_FOUND(
    "RN-OPENINGBALANCE-002", "error.openingBalance.not.found",
    "El balance inicial solicitado no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    OpeningBalanceError(String code, String messageKey, String defaultMessage,
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