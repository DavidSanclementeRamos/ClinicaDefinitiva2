package com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

// CORREGIDO: ERR_ACCOUNT_MISSING_CODE duplicado RN-LEDGERACCOUNT-008 → RN-LEDGERACCOUNT-009
public enum LedgerAccountError implements ErrorCatalog {

    ERR_ACCOUNT_INVALID_CODE_LENGTH(
            "RN-LEDGERACCOUNT-001","error.ledgerAccount.invalidCodeLength",
            "El código de la cuenta debe tener longitud válida (1, 2, 4, 6 u 8 dígitos)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ACCOUNT_INVALID_CODE_FORMAT(
            "RN-LEDGERACCOUNT-002","error.ledgerAccount.invalidCodeFormat",
            "El código de la cuenta solo puede contener dígitos numéricos",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ACCOUNT_MISSING_NATURE(
            "RN-LEDGERACCOUNT-003","error.ledgerAccount.missingNature",
            "La naturaleza de la cuenta es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ACCOUNT_NOT_EDITABLE(
            "RN-LEDGERACCOUNT-004","error.ledgerAccount.notEditable",
            "La cuenta solo puede editarse si está activa",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON(
            "RN-LEDGERACCOUNT-005","error.ledgerAccount.inactivationRequiresReason",
            "La inactivación de la cuenta requiere un motivo obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ACCOUNT_ALREADY_ACTIVE(
            "RN-LEDGERACCOUNT-006","error.ledgerAccount.alreadyActive",
            "La cuenta ya está activa",
            HttpStatus.CONFLICT, ErrorSeverity.WARN),

    ERR_ACCOUNT_REQUIRES_THIRD_PARTY(
            "RN-LEDGERACCOUNT-007","error.ledgerAccount.requiresThirdParty",
            "El movimiento debe cumplir requisitos de tercero si la cuenta lo requiere",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_ACCOUNT_REQUIRES_DOCUMENT(
            "RN-LEDGERACCOUNT-008","error.ledgerAccount.requiresDocument",
            "El movimiento debe cumplir requisitos de documento si la cuenta lo requiere",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    // CORREGIDO: duplicado RN-LEDGERACCOUNT-008 → RN-LEDGERACCOUNT-009
    ERR_ACCOUNT_MISSING_CODE(
            "RN-LEDGERACCOUNT-009","error.ledgerAccount.missingCode",
            "El código de la cuenta es obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_ACCOUNT_NOT_FOUND(
    "RN-LEDGERACCOUNT-010", "error.ledgerAccount.not.found",
    "La cuenta contable solicitada no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    LedgerAccountError(String code, String messageKey, String defaultMessage,
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