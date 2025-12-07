package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance;


import java.math.BigDecimal;

public record OpeningBalanceResponse(
        String id,
        String companyId,
        String accountId,
        String accountCode,
        String accountName,
        String thirdPartiesId,
        String thirdPartiesName,
        BigDecimal amount,
        String currency,
        String date
) {}

