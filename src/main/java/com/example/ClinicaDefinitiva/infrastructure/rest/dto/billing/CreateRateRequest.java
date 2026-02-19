package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing;

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
