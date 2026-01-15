package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum JournalEntryError implements ErrorCatalog {
    ERR_JOURNALENTRY_MISSING_ACCOUNT("RN-JOURNALENTRY-001", "error.journalEntry.missingAccount",
            "Debe especificarse una cuenta contable válida"),

    ERR_JOURNALENTRY_INVALID_AMOUNT("RN-JOURNALENTRY-002", "error.journalEntry.invalidAmount",
            "El monto debe ser mayor a cero"),

    ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH("RN-JOURNALENTRY-003", "error.journalEntry.debitCreditMismatch",
            "Los débitos y créditos deben estar balanceados"),

    ERR_JOURNALENTRY_DUPLICATE_REFERENCE("RN-JOURNALENTRY-004", "error.journalEntry.duplicateReference",
            "La referencia del asiento contable ya existe"),

    ERR_JOURNALENTRY_DATE_IN_FUTURE("RN-JOURNALENTRY-005", "error.journalEntry.dateInFuture",
            "La fecha del asiento no puede estar en el futuro"),

    ERR_JOURNALENTRY_DATE_BEFORE_PERIOD("RN-JOURNALENTRY-006", "error.journalEntry.dateBeforePeriod",
            "La fecha del asiento no puede ser anterior al inicio del período contable"),

    ERR_JOURNALENTRY_MISSING_DESCRIPTION("RN-JOURNALENTRY-007", "error.journalEntry.missingDescription",
            "La descripción del asiento es obligatoria"),

    ERR_JOURNALENTRY_NOT_EDITABLE("RN-JOURNALENTRY-008", "error.journalEntry.notEditable",
            "El asiento no puede editarse una vez publicado"),

    ERR_JOURNALENTRY_CANNOT_DELETE("RN-JOURNALENTRY-009", "error.journalEntry.cannotDelete",
            "El asiento no puede eliminarse si está conciliado"),

    ERR_JOURNALENTRY_UNAUTHORIZED_USER("RN-JOURNALENTRY-010", "error.journalEntry.unauthorizedUser",
            "El usuario no tiene permisos para registrar asientos contables"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_LINE_NOT_FOUND("RN-JOURNALENTRY-011", "error.journalEntry.lineNotFound",
            "La línea no existe en el asiento"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_EMPTY("RN-JOURNALENTRY-012", "error.journalEntry.empty",
            "El asiento debe tener al menos una línea"),

    ERR_JOURNALENTRY_INSUFFICIENT_LINES("RN-JOURNALENTRY-013", "error.journalEntry.insufficientLines",
            "El asiento debe tener al menos dos líneas (partida doble)"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_ALREADY_POSTED("RN-JOURNALENTRY-014", "error.journalEntry.alreadyPosted",
            "El asiento ya está contabilizado"),

    ERR_JOURNALENTRY_FUTURE_DATE("RN-JOURNALENTRY-015", "error.journalEntry.futureDate",
            "No se puede contabilizar un asiento con fecha futura"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_NOT_POSTED_REVERSAL("RN-JOURNALENTRY-016", "error.journalEntry.notPostedReversal",
            "Solo se pueden reversar asientos contabilizados"),

    ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON("RN-JOURNALENTRY-017", "error.journalEntry.reversalRequiresReason",
            "Se requiere una razón para reversar el asiento"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER("RN-JOURNALENTRY-018", "error.journalEntry.missingDocumentNumber",
            "El número de documento es obligatorio"),

    ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER("RN-JOURNALENTRY-019", "error.journalEntry.invalidDocumentNumber",
            "El número de documento debe tener al menos 1 carácter"),

    ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD("RN-JOURNALENTRY-020", "error.journalEntry.missingDescriptionField",
            "La descripción es obligatoria"),

    ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH("RN-JOURNALENTRY-021", "error.journalEntry.invalidDescriptionLength",
            "La descripción debe tener al menos 5 caracteres"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_MISSING_DATE("RN-JOURNALENTRY-022", "error.journalEntry.missingDate",
            "La fecha es obligatoria"),
    // --- JournalEntry ---
    ERR_JOURNALENTRY_MISSING_AMOUNT("RN-JOURNALENTRY-023", "error.journalEntry.missingAmount",
            "El monto es obligatorio");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    JournalEntryError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() { return code; }
    @Override
    public String getMessageKey() { return messageKey; }
    @Override
    public String getDefaultMessage() { return defaultMessage; }



}
