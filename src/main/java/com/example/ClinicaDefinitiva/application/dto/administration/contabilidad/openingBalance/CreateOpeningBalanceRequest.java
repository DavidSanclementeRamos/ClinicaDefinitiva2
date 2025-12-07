package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance;

import java.math.BigDecimal;

public record CreateOpeningBalanceRequest(
        String companyId,
        String accountId,
        String thirdPartiesId,
        BigDecimal amount,
        String currency
) {}
