package com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance;

import java.math.BigDecimal;

public record PageOpeningBalanceDto(
        Long id,
        BigDecimal amount,
        String currency
) {}
