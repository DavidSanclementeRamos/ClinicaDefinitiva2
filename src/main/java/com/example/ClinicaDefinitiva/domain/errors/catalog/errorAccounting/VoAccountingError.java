package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoAccountingError implements ErrorCatalog {

    // ===== AdministrativeReportId =====
ERR_ADMINREPORT_ID_NULL(
        "ERR-ACCOUNTING-VO-001",
        "error.adminreport.id.null",
        "El identificador del reporte administrativo no puede ser nulo"
),
ERR_ADMINREPORT_ID_INVALID(
        "ERR-ACCOUNTING-VO-002",
        "error.adminreport.id.invalid",
        "El identificador del reporte administrativo es inválido"
),

// ===== ReportStatus =====
ERR_REPORT_STATUS_NULL(
        "ERR-ACCOUNTING-VO-003",
        "error.report.status.null",
        "El estado del reporte no puede ser nulo"
),

// ===== Indicator =====
ERR_REPORT_INDICATOR_NULL(
        "ERR-ACCOUNTING-VO-004",
        "error.report.indicator.null",
        "El indicador no puede ser nulo"
),
ERR_REPORT_INDICATOR_INVALID(
        "ERR-ACCOUNTING-VO-005",
        "error.report.indicator.invalid",
        "El formato es invalido"
),

// ===== CompanyId =====
ERR_COMPANY_ID_NULL(
        "ERR-ACCOUNTING-VO-006",
        "error.company.id.null",
        "El identificador de la compañía no puede ser nulo"
),
ERR_COMPANY_ID_INVALID(
        "ERR-ACCOUNTING-VO-007",
        "error.company.id.invalid",
        "El identificador de la compañía es inválido"
),

// ===== CompanyStatus =====
ERR_COMPANY_STATUS_NULL(
        "ERR-ACCOUNTING-VO-008",
        "error.company.status.null",
        "El estado de la compañía no puede ser nulo"
),
ERR_COMPANY_STATUS_INVALID(
        "ERR-ACCOUNTING-VO-009",
        "error.company.status.invalid",
        "El estado de la compañía es inválido"
),

// ===== ContractId =====
ERR_CONTRACT_ID_NULL(
        "ERR-ACCOUNTING-VO-010",
        "error.contract.id.null",
        "El identificador del contrato no puede ser nulo"
),
ERR_CONTRACT_ID_INVALID(
        "ERR-ACCOUNTING-VO-011",
        "error.contract.id.invalid",
        "El identificador del contrato es inválido"
),

// ===== ExpenseId =====
ERR_EXPENSE_ID_NULL(
        "ERR-ACCOUNTING-VO-012",
        "error.expense.id.null",
        "El identificador del gasto no puede ser nulo"
),
ERR_EXPENSE_ID_INVALID(
        "ERR-ACCOUNTING-VO-013",
        "error.expense.id.invalid",
        "El identificador del gasto es inválido"
),

// ===== JournalEntryId =====
ERR_JOURNAL_ENTRY_ID_NULL(
        "ERR-ACCOUNTING-VO-014",
        "error.journalEntry.id.null",
        "El identificador del asiento contable no puede ser nulo"
),
ERR_JOURNAL_ENTRY_ID_INVALID(
        "ERR-ACCOUNTING-VO-015",
        "error.journalEntry.id.invalid",
        "El identificador del asiento contable es inválido"
),

// ===== LedgerAccountId =====
ERR_LEDGER_ACCOUNT_ID_NULL(
        "ERR-ACCOUNTING-VO-016",
        "error.ledgerAccount.id.null",
        "El identificador de la cuenta contable no puede ser nulo"
),
ERR_LEDGER_ACCOUNT_ID_INVALID(
        "ERR-ACCOUNTING-VO-017",
        "error.ledgerAccount.id.invalid",
        "El identificador de la cuenta contable es inválido"
),

// ===== OpeningBalanceId =====
ERR_OPENING_BALANCE_ID_NULL(
        "ERR-ACCOUNTING-VO-018",
        "error.openingBalance.id.null",
        "El identificador del balance inicial no puede ser nulo"
),
ERR_OPENING_BALANCE_ID_INVALID(
        "ERR-ACCOUNTING-VO-019",
        "error.openingBalance.id.invalid",
        "El identificador del balance inicial es inválido"
),

// ===== ThirdPartiesId =====
ERR_THIRDPARTIES_ID_NULL(
        "ERR-ACCOUNTING-VO-020",
        "error.thirdParties.id.null",
        "El identificador del tercero no puede ser nulo"
),
ERR_THIRDPARTIES_ID_INVALID(
        "ERR-ACCOUNTING-VO-021",
        "error.thirdParties.id.invalid",
        "El identificador del tercero es inválido"
),

// ===== Nit =====
ERR_NIT_NULL(
        "ERR-ACCOUNTING-VO-022",
        "error.nit.null",
        "El NIT no puede ser nulo"
),
ERR_NIT_INVALID_FORMAT(
        "ERR-ACCOUNTING-VO-023",
        "error.nit.invalidFormat",
        "El NIT no cumple con el formato esperado"
),



// ===== Period =====
ERR_PERIOD_NULL(
        "ERR-ACCOUNTING-VO-027",
        "error.period.null",
        "Las fechas de inicio y fin del período no pueden ser nulas"
),
ERR_PERIOD_INVALID(
        "ERR-ACCOUNTING-VO-028",
        "error.period.invalid",
        "La fecha de fin no puede ser anterior a la fecha de inicio"
),

ERR_DOCUMENT_NULL(
        "ERR-ACCOUNTING-VO-029",
        "error.document.null",
        "El documento no puede ser nulo"
),

ERR_DOCUMENT_INVALID_FORMAT(
        "ERR-ACCOUNTING-VO-030",
        "error.document.invalidFormat",
        "El formato del documento es inválido"
);

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


