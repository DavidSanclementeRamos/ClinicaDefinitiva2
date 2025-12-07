package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance;

import java.math.BigDecimal;

public record OpeningBalanceListResponse(
        String id,
        String accountCode,
        String accountName,
        BigDecimal amount,
        String currency
) {}
