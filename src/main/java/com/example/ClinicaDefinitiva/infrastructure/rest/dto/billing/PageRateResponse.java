package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PageRateResponse(
        Long id,
        Long serviceId,
        String payerType,
        BigDecimal amount,
        String currency,
        LocalDateTime validFrom,
        LocalDateTime validTo,
        Boolean active
) {
}
