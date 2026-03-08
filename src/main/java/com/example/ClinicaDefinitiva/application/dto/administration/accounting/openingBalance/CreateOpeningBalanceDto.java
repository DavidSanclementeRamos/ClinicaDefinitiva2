package com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance;

import java.math.BigDecimal;

public record CreateOpeningBalanceDto(
        Long companyId,
        Long accountId,
        Long thirdPartiesId,
        BigDecimal amount,
        String currency
) {}
