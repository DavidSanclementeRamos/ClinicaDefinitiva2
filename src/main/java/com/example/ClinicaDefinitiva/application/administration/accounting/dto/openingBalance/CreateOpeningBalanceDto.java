package com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance;

import java.math.BigDecimal;

public record CreateOpeningBalanceDto(
        Long companyId,
        Long accountId,
        Long thirdPartiesId,
        BigDecimal amount,
        String currency
) {}
