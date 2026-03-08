package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum CompanyError implements ErrorCatalog {

    ERR_COMPANY_INVALID_INCORPORATION_DATE(
            "RN-COMPANY-001","error.company.invalidIncorporationDate",
            "La fecha de constitución no es válida (no puede ser anterior a 1800)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_COMPANY_FUTURE_INCORPORATION_DATE(
            "RN-COMPANY-002","error.company.futureIncorporationDate",
            "La fecha de constitución no puede ser futura",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_COMPANY_NOT_EDITABLE(
            "RN-COMPANY-003","error.company.notEditable",
            "La empresa solo puede editarse si está en estado ACTIVE o SUSPENDED",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY(
            "RN-COMPANY-004","error.company.cannotReactivateDirectly",
            "Una empresa inactiva no puede reactivarse sin proceso formal",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_COMPANY_MISSING_INCORPORATION_DATE(
            "RN-COMPANY-005","error.company.missingIncorporationDate",
            "La fecha de constitución es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    CompanyError(String code, String messageKey, String defaultMessage,
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