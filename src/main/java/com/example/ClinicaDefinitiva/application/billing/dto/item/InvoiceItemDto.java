package com.example.ClinicaDefinitiva.application.billing.dto.item;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceItemDto(
        Long id,
        Long serviceId,
        String serviceCode,
        String serviceDescription,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalPrice,
        Long rateId,
        LocalDateTime performedAt
) {}

