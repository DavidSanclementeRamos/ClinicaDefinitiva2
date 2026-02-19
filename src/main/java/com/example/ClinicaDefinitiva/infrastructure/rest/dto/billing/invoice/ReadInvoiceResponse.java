package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice;

import com.example.ClinicaDefinitiva.application.dto.billing.item.InvoiceItemDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.InvoiceItemResponse;

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
