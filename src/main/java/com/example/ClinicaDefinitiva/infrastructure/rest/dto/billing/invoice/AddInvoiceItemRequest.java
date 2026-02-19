package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice;

import java.time.LocalDateTime;

public record AddInvoiceItemRequest(
        Long item,
        Long serviceId,
        Long rateId,
        Integer quantity,
        LocalDateTime performedAt
) {
}
