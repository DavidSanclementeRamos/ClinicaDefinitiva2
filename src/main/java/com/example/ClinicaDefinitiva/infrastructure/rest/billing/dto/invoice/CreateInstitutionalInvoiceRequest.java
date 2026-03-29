package com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice;

import java.time.LocalDateTime;

public record CreateInstitutionalInvoiceRequest (
        Long patientId,
        Long dentistId,
        Long providerId,
        Long contractId,
        String currency,
        LocalDateTime dueDate,
        String notes
){
}
