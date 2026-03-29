package com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;

/**
 * DTO para reporte de balance
 */
public record BalanceReportResponse(
        String accountCode,
        String accountName,
        BigDecimal debits,
        BigDecimal credits,
        BigDecimal balance
) {}

