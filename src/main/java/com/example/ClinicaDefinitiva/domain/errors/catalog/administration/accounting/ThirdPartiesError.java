package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ThirdPartiesError implements ErrorCatalog {

    ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH(
            "RN-THIRDPARTIES-001","error.thirdParties.invalidDocumentLength",
            "Número de documento debe tener entre 5 y 20 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE(
            "RN-THIRDPARTIES-002","error.thirdParties.missingDocumentType",
            "Tipo de documento es obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER(
            "RN-THIRDPARTIES-003","error.thirdParties.missingDocumentNumber",
            "Número de documento es obligatorio y único",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_THIRD_PARTY_MISSING_TYPE(
            "RN-THIRDPARTIES-004","error.thirdParties.missingType",
            "Tipo de tercero es obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_THIRD_PARTY_NOT_EDITABLE(
            "RN-THIRDPARTIES-005","error.thirdParties.notEditable",
            "Solo puede editarse si está activo",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON(
            "RN-THIRDPARTIES-006","error.thirdParties.inactivationRequiresReason",
            "Inactivación requiere motivo obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_THIRD_PARTY_ALREADY_ACTIVE(
            "RN-THIRDPARTIES-007","error.thirdParties.alreadyActive",
            "El tercero ya está activo",
            HttpStatus.CONFLICT, ErrorSeverity.WARN),

    ERR_THIRD_PARTY_ALREADY_INACTIVE(
            "RN-THIRDPARTIES-011","error.thirdParties.alreadyInactive",
            "El tercero ya está inactivo",
            HttpStatus.CONFLICT, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ThirdPartiesError(String code, String messageKey, String defaultMessage,
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