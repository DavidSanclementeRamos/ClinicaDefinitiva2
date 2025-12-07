package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ReportStatus;

import java.util.List;

public record ReportResponse(
        String id,
        NameDto title,
        PeriodDto period,
        String createdAt,
        String createdBy,
        ReportStatus status,
        List<String> journalEntryReferences,
        List<IndicatorDto> indicators,
        String notes,
        List<DocumentDto> attachments,
        String lastUpdate,
        String approvedBy,
        boolean isComplete,
        boolean isEditable
) {}