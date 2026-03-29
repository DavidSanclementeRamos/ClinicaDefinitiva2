package com.example.ClinicaDefinitiva.application.billing.dto.invoice;

import java.time.LocalDateTime;

public record CreateInstitutionalInvoiceDto(
        Long patientId,
        Long dentistId,
        Long providerId,
        Long contractId,
        String currency,
        LocalDateTime dueDate,
        String notes
) {}

