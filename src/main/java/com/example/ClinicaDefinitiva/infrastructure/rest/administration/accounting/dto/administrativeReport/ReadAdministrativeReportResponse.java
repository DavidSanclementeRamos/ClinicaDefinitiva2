package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReadAdministrativeReportResponse(
    Long id,
    String title,
    LocalDate periodStart,
    LocalDate periodEnd,
    LocalDateTime createdAt,
    Long createdBy,
    String status,
    List<Long> journalEntryReferences,
    List<IndicatorResponse> indicators,
    String notes,
    List<AttachmentResponse> attachments,
    LocalDateTime lastUpdate,
    Long approvedBy,
    boolean isComplete,
    boolean isEditable
) {}
