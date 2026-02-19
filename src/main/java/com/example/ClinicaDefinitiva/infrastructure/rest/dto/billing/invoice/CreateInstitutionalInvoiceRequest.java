package com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.invoice;

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
