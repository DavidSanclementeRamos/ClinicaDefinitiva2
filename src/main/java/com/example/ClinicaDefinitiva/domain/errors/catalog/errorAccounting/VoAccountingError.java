package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoAccountingError implements ErrorCatalog {

    // ===== AdministrativeReportId =====
    ERR_ADMINREPORT_ID_NULL("RN-ADMINREPORT-001", "error.adminreport.id.null",
            "El identificador del reporte administrativo no puede ser nulo"),
    ERR_ADMINREPORT_ID_INVALID("RN-ADMINREPORT-002", "error.adminreport.id.invalid",
            "El identificador del reporte administrativo es inválido"),
    // ===== ReportStatus =====
    ERR_REPORT_STATUS_NULL("RN-REPORT-001", "error.report.status.null",
            "El estado del reporte no puede ser nulo"),
    // ===== Indicator =====
    ERR_REPORT_INDICATOR_NULL("RN-REPORT-002", "error.report.indicator.null",
            "El indicador no puede ser nulo"),

    ERR_REPORT_INDICATOR_INVALID("RN-REPORT-003","error.report.indicator.invalid","El formato es invalido"),

    // ===== CompanyId =====
    ERR_COMPANY_ID_NULL("RN-COMPANY-001", "error.company.id.null",
            "El identificador de la compañía no puede ser nulo"),
    ERR_COMPANY_ID_INVALID("RN-COMPANY-002", "error.company.id.invalid",
            "El identificador de la compañía es inválido"),

    // ===== CompanyStatus =====
    ERR_COMPANY_STATUS_NULL("RN-COMPANY-003", "error.company.status.null",
            "El estado de la compañía no puede ser nulo"),
    ERR_COMPANY_STATUS_INVALID("RN-COMPANY-004", "error.company.status.invalid",
            "El estado de la compañía es inválido"),

    // ===== ContractId =====
    ERR_CONTRACT_ID_NULL("RN-CONTRACT-001", "error.contract.id.null",
            "El identificador del contrato no puede ser nulo"),
    ERR_CONTRACT_ID_INVALID("RN-CONTRACT-002", "error.contract.id.invalid",
            "El identificador del contrato es inválido"),

    // ===== ExpenseId =====
    ERR_EXPENSE_ID_NULL("RN-EXPENSE-001", "error.expense.id.null",
            "El identificador del gasto no puede ser nulo"),
    ERR_EXPENSE_ID_INVALID("RN-EXPENSE-002", "error.expense.id.invalid",
            "El identificador del gasto es inválido"),

    // ===== JournalEntryId =====
    ERR_JOURNAL_ENTRY_ID_NULL("RN-JOURNAL-001", "error.journalEntry.id.null",
            "El identificador del asiento contable no puede ser nulo"),
    ERR_JOURNAL_ENTRY_ID_INVALID("RN-JOURNAL-002", "error.journalEntry.id.invalid",
            "El identificador del asiento contable es inválido"),

    // ===== LedgerAccountId =====
    ERR_LEDGER_ACCOUNT_ID_NULL("RN-LEDGER-001", "error.ledgerAccount.id.null",
            "El identificador de la cuenta contable no puede ser nulo"),
    ERR_LEDGER_ACCOUNT_ID_INVALID("RN-LEDGER-002", "error.ledgerAccount.id.invalid",
            "El identificador de la cuenta contable es inválido"),

    // ===== OpeningBalanceId =====
    ERR_OPENING_BALANCE_ID_NULL("RN-OPENINGBALANCE-001", "error.openingBalance.id.null",
            "El identificador del balance inicial no puede ser nulo"),
    ERR_OPENING_BALANCE_ID_INVALID("RN-OPENINGBALANCE-002", "error.openingBalance.id.invalid",
            "El identificador del balance inicial es inválido"),

    // ===== ThirdPartiesId =====
    ERR_THIRDPARTIES_ID_NULL("RN-THIRDPARTIES-001", "error.thirdParties.id.null",
            "El identificador del tercero no puede ser nulo"),
    ERR_THIRDPARTIES_ID_INVALID("RN-THIRDPARTIES-002", "error.thirdParties.id.invalid",
            "El identificador del tercero es inválido"),

    // ===== Nit =====
    ERR_NIT_NULL("RN-NIT-001", "error.nit.null",
            "El NIT no puede ser nulo"),
    ERR_NIT_INVALID_FORMAT("RN-NIT-002", "error.nit.invalidFormat",
            "El NIT no cumple con el formato esperado"),

    // ===== Name =====
    ERR_NAME_NULL("RN-NAME-001", "error.name.null",
            "El nombre no puede ser nulo"),
    ERR_NAME_BLANK("RN-NAME-002", "error.name.blank",
            "El nombre no puede estar vacío"),
    ERR_NAME_TOO_LONG("RN-NAME-003", "error.name.tooLong",
            "El nombre excede la longitud máxima permitida"),


    // ===== Period =====
    ERR_PERIOD_NULL("RN-PERIOD-001", "error.period.null",
                            "Las fechas de inicio y fin del período no pueden ser nulas"),
    ERR_PERIOD_INVALID("RN-PERIOD-002", "error.period.invalid",
                               "La fecha de fin no puede ser anterior a la fecha de inicio"),
    ERR_DOCUMENT_NULL("","" ,"" ),

    ERR_DOCUMENT_INVALID_FORMAT("","","");

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


