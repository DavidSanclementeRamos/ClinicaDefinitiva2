package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PageOpeningBalanceResponse(
    Long id,
    BigDecimal amount,
    String currency
) {}
