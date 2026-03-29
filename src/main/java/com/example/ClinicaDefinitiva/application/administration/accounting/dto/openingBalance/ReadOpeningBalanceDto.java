package com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance;


import java.math.BigDecimal;
import java.time.LocalDate;

public record ReadOpeningBalanceDto(
        Long id,
        Long companyId,
        Long accountId,
        Long thirdPartiesId,
        BigDecimal amount,
        String currency,
        LocalDate date
) {}

