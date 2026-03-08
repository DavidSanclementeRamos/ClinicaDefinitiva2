package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance;


import java.math.BigDecimal;

public record ReadOpeningBalanceDto(
        Long id,
        Long companyId,
        Long accountId,
        Long thirdPartiesId,
        BigDecimal amount,
        String currency,
        String date
) {}

