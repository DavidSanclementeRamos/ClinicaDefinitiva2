package com.example.ClinicaDefinitiva.application.dto.billing.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PageInvoiceDto(
        Long id,
        String invoiceNumber,
        Long dentistId,
        Long providerId,
        String status,
        BigDecimal total,
        String currency,
        LocalDateTime dueDate,
        LocalDateTime createdAt
) {}
