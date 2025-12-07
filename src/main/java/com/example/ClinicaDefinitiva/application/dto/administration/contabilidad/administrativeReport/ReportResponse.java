package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.AuditoriaInfo;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ReportStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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