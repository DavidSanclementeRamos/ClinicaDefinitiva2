package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance;

import java.math.BigDecimal;

public record PageOpeningBalanceDto(
        Long id,
        BigDecimal amount,
        String currency
) {}
