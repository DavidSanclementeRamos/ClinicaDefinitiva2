package com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.rate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceItemResponse(
        Long id,
        Long serviceId,
        String serviceCode,
        String serviceDescription,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice,
        Long rateId,
        LocalDateTime performedAt
) {
}
