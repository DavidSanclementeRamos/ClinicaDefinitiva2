package com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport;

import java.time.LocalDateTime;
import java.util.List;

public record ReadAdministrativeReportDto(
        Long id,
        String title,
        PeriodDto period,
        LocalDateTime createdAt,
        Long createdBy,
        String status,
        List<Long> journalEntryReferences,
        List<IndicatorDto> indicators,
        String notes,
        List<DocumentDto> attachments,
        LocalDateTime lastUpdate,
        Long approvedBy,
        boolean isComplete,
        boolean isEditable
) {}