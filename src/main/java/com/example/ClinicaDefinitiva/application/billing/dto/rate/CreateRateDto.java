package com.example.ClinicaDefinitiva.application.billing.dto.rate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateRateDto(
        Long serviceId,
        String payerType,
        Long contractId,
        BigDecimal amount,
        String currency
        
) {}


