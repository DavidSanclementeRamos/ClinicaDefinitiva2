package com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice;

import com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.rate.InvoiceItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReadInvoiceResponse(
        Long contractId,

        String status,
        String currency,

        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal total,

        LocalDateTime dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        String notes,

        List<InvoiceItemResponse> items
) {
}
