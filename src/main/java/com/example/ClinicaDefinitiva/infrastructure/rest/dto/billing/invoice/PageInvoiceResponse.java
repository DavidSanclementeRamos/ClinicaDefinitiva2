package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PageInvoiceResponse(

        Long id,
        String invoiceNumber,
        Long dentistId,
        Long providerId,
        String status,
        BigDecimal total,
        String currency,
        LocalDateTime dueDate,
        LocalDateTime createdAt
) {
}
