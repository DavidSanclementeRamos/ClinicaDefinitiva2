package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReadOpeningBalanceResponse(
    Long id,
    Long companyId,
    Long accountId,
    Long thirdPartyId,
    BigDecimal amount,
    String currency,
    LocalDate date
) {}
