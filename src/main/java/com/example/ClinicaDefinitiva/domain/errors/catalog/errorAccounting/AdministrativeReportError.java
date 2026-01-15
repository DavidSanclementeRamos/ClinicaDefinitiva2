package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AdministrativeReportError implements ErrorCatalog {
    ERR_REPORT_NOT_EDITABLE("RN-ADMINREPORT-001", "error.report.notEditable",
            "Solo puede editarse si está en estado DRAFT"),

    ERR_REPORT_INCOMPLETE("RN-ADMINREPORT-002", "error.report.incomplete",
            "El reporte debe tener al menos un asiento contable o un indicador"),

    ERR_REPORT_CANNOT_SUBMIT("RN-ADMINREPORT-003", "error.report.cannotSubmit",
            "Solo puede enviarse a revisión desde DRAFT"),

    ERR_REPORT_CANNOT_APPROVE("RN-ADMINREPORT-004", "error.report.cannotApprove",
            "Solo puede aprobarse si está en revisión"),

    ERR_REPORT_CANNOT_REJECT("RN-ADMINREPORT-005", "error.report.cannotReject",
            "Solo puede rechazarse si está en revisión"),

    ERR_REPORT_REJECTION_REQUIRES_REASON("RN-ADMINREPORT-006", "error.report.rejectionRequiresReason",
            "Se requiere una razón para rechazar el reporte"),

    ERR_REPORT_CANNOT_ARCHIVE("RN-ADMINREPORT-007", "error.report.cannotArchive",
            "Solo puede archivarse si está publicado"),

    ERR_REPORT_CANNOT_UNARCHIVE("RN-ADMINREPORT-008", "error.report.cannotUnarchive",
            "Solo puede desarchivarse si está archivado"),

    ERR_REPORT_MISSING_APPROVER("RN-ADMINREPORT-011", "error.report.missingApprover",
            "La aprobación requiere usuario aprobador válido");


    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    AdministrativeReportError(String code, String messageKey, String defaultMessage) {
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
