package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;


public enum JournalEntryError implements ErrorCatalog {

    ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH(
            "RN-JOURNALENTRY-001","error.journalEntry.debitCreditMismatch",
            "Los débitos y créditos deben estar balanceados",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_NOT_EDITABLE(
            "RN-JOURNALENTRY-002","error.journalEntry.notEditable",
            "El asiento no puede editarse una vez publicado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_LINE_NOT_FOUND(
            "RN-JOURNALENTRY-003","error.journalEntry.lineNotFound",
            "La línea no existe en el asiento",
            HttpStatus.NOT_FOUND, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_EMPTY(
            "RN-JOURNALENTRY-004","error.journalEntry.empty",
            "El asiento debe tener al menos una línea",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_INSUFFICIENT_LINES(
            "RN-JOURNALENTRY-005","error.journalEntry.insufficientLines",
            "El asiento debe tener al menos dos líneas (partida doble)",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_ALREADY_POSTED(
            "RN-JOURNALENTRY-006","error.journalEntry.alreadyPosted",
            "El asiento ya está contabilizado",
            HttpStatus.CONFLICT, ErrorSeverity.WARN),

    ERR_JOURNALENTRY_FUTURE_DATE(
            "RN-JOURNALENTRY-007","error.journalEntry.futureDate",
            "No se puede contabilizar un asiento con fecha futura",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_NOT_POSTED_REVERSAL(
            "RN-JOURNALENTRY-008","error.journalEntry.notPostedReversal",
            "Solo se pueden reversar asientos contabilizados",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON(
            "RN-JOURNALENTRY-009","error.journalEntry.reversalRequiresReason",
            "Se requiere una razón para reversar el asiento",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER(
            "RN-JOURNALENTRY-010","error.journalEntry.missingDocumentNumber",
            "El número de documento es obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER(
            "RN-JOURNALENTRY-011","error.journalEntry.invalidDocumentNumber",
            "El número de documento debe tener al menos 1 carácter",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD(
            "RN-JOURNALENTRY-012","error.journalEntry.missingDescriptionField",
            "La descripción es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH(
            "RN-JOURNALENTRY-013","error.journalEntry.invalidDescriptionLength",
            "La descripción debe tener al menos 5 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_JOURNALENTRY_MISSING_DATE(
            "RN-JOURNALENTRY-014","error.journalEntry.missingDate",
            "La fecha es obligatoria",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    JournalEntryError(String code, String messageKey, String defaultMessage,
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
