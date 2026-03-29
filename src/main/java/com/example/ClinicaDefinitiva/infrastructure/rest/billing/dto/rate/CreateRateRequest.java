package com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.rate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateRateRequest(
        Long serviceId,
        String payerType,
        Long contractId,
        BigDecimal amount,
        String currency,
        LocalDateTime validFrom,
        LocalDateTime validTo
) {
}
