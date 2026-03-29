package com.example.ClinicaDefinitiva.application.billing.dto.invoice;

import java.time.LocalDateTime;

public record CreateParticularInvoiceDto(
        Long patientId,
        Long dentistId,
        Long providerId,
        String currency,
        LocalDateTime dueDate,
        String notes
) {
}
