package com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance;

import java.math.BigDecimal;

public record PageOpeningBalanceDto(
        Long id,
        BigDecimal amount,
        String currency
) {}
