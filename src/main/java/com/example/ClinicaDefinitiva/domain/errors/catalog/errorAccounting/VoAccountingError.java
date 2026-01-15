package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoAccountingError implements ErrorCatalog {

    //  Errores de asientos contables
    ERR_REPORT_DUPLICATE_JOURNAL_ENTRY("RN-ADMINREPORT-009", "error.report.duplicateJournalEntry",
            "No puede agregarse referencia duplicada a asiento contable"),

    ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND("RN-ADMINREPORT-010", "error.report.journalEntryNotFound",
            "El asiento contable no está referenciado en el reporte"),


    //  Errores de indicadores
    ERR_REPORT_INDICATOR_NULL("RN-ADMINREPORT-XXX", "error.report.indicatorNull",
            "El indicador no puede ser nulo"),

    ERR_REPORT_INDICATOR_NOT_FOUND("RN-ADMINREPORT-XXX", "error.report.indicatorNotFound",
            "El indicador no existe en el reporte"),
    // DocumentId errors (nuevo)
    ERR_DOCUMENT_NULL(
            "RN-VO-012",
            "error.document.null",
            "El documento de identidad no puede ser nulo"
    ),
    ERR_DOCUMENT_INVALID_FORMAT(
            "RN-VO-013",
            "error.document.format",
            "El formato del documento de identidad es inválido"
    ),

    ERR_DOCUMENT_BLANK("","",""),

    //  Errores de documentos adjuntos
    ERR_REPORT_ATTACHMENT_NULL("RN-ADMINREPORT-XXX", "error.report.attachmentNull",
            "El documento no puede ser nulo"),

    ERR_REPORT_ATTACHMENT_NOT_FOUND("RN-ADMINREPORT-XXX", "error.report.attachmentNotFound",
            "El documento no existe en el reporte");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    VoAccountingError(String code, String messageKey, String defaultMessage) {
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
