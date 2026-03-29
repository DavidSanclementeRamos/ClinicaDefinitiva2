package com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice;

import java.time.LocalDateTime;

public record CreateParticularInvoiceRequest(
        Long patientId,
        Long dentistId,
        Long providerId,
        String currency,
        LocalDateTime dueDate,
        String notes
) {
}
