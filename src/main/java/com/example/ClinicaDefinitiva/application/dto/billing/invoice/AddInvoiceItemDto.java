package com.example.ClinicaDefinitiva.application.dto.billing.invoice;


import java.time.LocalDateTime;

public record AddInvoiceItemDto(
        Long item,
        Long serviceId,
        Long rateId,
        Integer quantity,
        LocalDateTime performedAt
) {}

