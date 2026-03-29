package com.example.ClinicaDefinitiva.application.billing.dto.rate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PageRateDto(
        Long id,
        Long serviceId,
        String payerType,
        BigDecimal amount,
        String currency,
        LocalDateTime validFrom,
        LocalDateTime validTo,
        Boolean active
) {}

