package com.example.ClinicaDefinitiva.application.dto.billing.invoice;

import com.example.ClinicaDefinitiva.application.dto.billing.item.InvoiceItemDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReadInvoiceDto(
        Long id,
        String invoiceNumber,

        Long patientId,

        Long dentistId,

        Long providerId,

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

        List<InvoiceItemDto> items
) {}

