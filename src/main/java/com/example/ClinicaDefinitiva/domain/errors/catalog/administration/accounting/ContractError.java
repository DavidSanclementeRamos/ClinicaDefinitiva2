package com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ContractError implements ErrorCatalog {

    ERR_CONTRACT_INVALID_DATES(
            "RN-CONTRACT-001","error.contract.invalidDates",
            "La fecha de fin debe ser posterior a la fecha de inicio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_CONTRACT_NOT_EDITABLE(
            "RN-CONTRACT-002","error.contract.notEditable",
            "Solo puede editarse si está en estado ACTIVE y no vencido",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_CONTRACT_CANNOT_SUSPEND(
            "RN-CONTRACT-003","error.contract.cannotSuspend",
            "Solo puede suspenderse si está en estado ACTIVE",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE(
            "RN-CONTRACT-004","error.contract.expiredCannotReactivate",
            "No puede reactivarse si está vencido",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_CONTRACT_EXPIRED_NOT_EDITABLE(
            "RN-CONTRACT-005","error.contract.expiredNotEditable",
            "No se puede editar un contrato vencido",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_CONTRACT_MISSING_COVERAGE_TYPE(
            "RN-CONTRACT-006","error.contract.missingCoverageType",
            "Debe tener tipo de cobertura válido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_CONTRACT_MISSING_END_DATE(
            "RN-CONTRACT-007","error.contract.missingEndDate",
            "La fecha de fin es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_CONTRACT_TERMINATION_REQUIRES_REASON(
            "RN-CONTRACT-008","error.contract.terminationRequiresReason",
            "La terminación requiere motivo obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_CONTRACT_MISSING_NEW_END_DATE(
            "RN-CONTRACT-009","error.contract.missingNewEndDate",
            "La nueva fecha de fin es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_CONTRACT_NEW_END_DATE_IN_PAST(
            "RN-CONTRACT-010","error.contract.newEndDateInPast",
            "La nueva fecha de fin no puede estar en el pasado",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_CONTRACT_CANNOT_REACTIVATE(
            "RN-CONTRACT-011","error.contract.cannotReactivate",
            "Solo se pueden reactivar contratos suspendidos",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_CONTRACT_ALREADY_TERMINATED(
            "RN-CONTRACT-012","error.contract.alreadyTerminated",
            "El contrato ya está terminado",
            HttpStatus.CONFLICT, ErrorSeverity.WARN),

    ERR_CONTRACT_MISSING_START_DATE(
            "RN-CONTRACT-013","error.contract.missingStartDate",
            "La fecha de inicio es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_CONTRACT_NOT_FOUND(
    "RN-CONTRACT-014", "error.contract.not.found",
    "El contrato solicitado no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ContractError(String code, String messageKey, String defaultMessage,
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