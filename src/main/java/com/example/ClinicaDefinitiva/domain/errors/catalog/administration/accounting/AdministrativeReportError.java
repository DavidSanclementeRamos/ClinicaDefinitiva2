package com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AdministrativeReportError implements ErrorCatalog {

    ERR_REPORT_NOT_EDITABLE(
            "RN-ADMINREPORT-001","error.report.notEditable",
            "Solo puede editarse si está en estado DRAFT",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_INDICATOR_NOT_FOUND(
            "RN-ADMINREPORT-002","error.report.indicatorNotFound",
            "El indicador especificado no se encuentra en el reporte",
            HttpStatus.NOT_FOUND, ErrorSeverity.ERROR),

    ERR_REPORT_CANNOT_SUBMIT(
            "RN-ADMINREPORT-003","error.report.cannotSubmit",
            "Solo puede enviarse a revisión desde DRAFT",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_CANNOT_APPROVE(
            "RN-ADMINREPORT-004","error.report.cannotApprove",
            "Solo puede aprobarse si está en revisión",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_CANNOT_REJECT(
            "RN-ADMINREPORT-005","error.report.cannotReject",
            "Solo puede rechazarse si está en revisión",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_REJECTION_REQUIRES_REASON(
            "RN-ADMINREPORT-006","error.report.rejectionRequiresReason",
            "Se requiere una razón para rechazar el reporte",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_REPORT_CANNOT_ARCHIVE(
            "RN-ADMINREPORT-007","error.report.cannotArchive",
            "Solo puede archivarse si está publicado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_CANNOT_UNARCHIVE(
            "RN-ADMINREPORT-008","error.report.cannotUnarchive",
            "Solo puede desarchivarse si está archivado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_MISSING_APPROVER(
            "RN-ADMINREPORT-009","error.report.missingApprover",
            "La aprobación requiere usuario aprobador válido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_REPORT_DUPLICATE_JOURNAL_ENTRY(
            "RN-ADMINREPORT-010","error.report.duplicateJournalEntry",
            "El reporte contiene un asiento contable duplicado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND(
            "RN-ADMINREPORT-011","error.report.journalEntryNotFound",
            "El asiento contable referenciado no existe en el reporte",
            HttpStatus.NOT_FOUND, ErrorSeverity.ERROR),

    ERR_REPORT_TOO_MANY_INDICATORS(
            "RN-ADMINREPORT-012","error.report.tooManyIndicators",
            "El reporte excede el número máximo permitido de indicadores",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_REPORT_ATTACHMENT_NOT_FOUND(
            "RN-ADMINREPORT-013","error.report.attachmentNotFound",
            "El documento adjunto especificado no se encuentra en el reporte",
            HttpStatus.NOT_FOUND, ErrorSeverity.ERROR),
    ERR_REPORT_NOT_FOUND("RN-REPORT-014", "error.report.not.found",
    "El reporte solicitado no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    AdministrativeReportError(String code, String messageKey, String defaultMessage,
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