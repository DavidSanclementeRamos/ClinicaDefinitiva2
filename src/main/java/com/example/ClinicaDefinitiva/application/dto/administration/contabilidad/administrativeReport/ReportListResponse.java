package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus;

public record ReportListResponse(
        String id,
        NameDto title,
        PeriodDto period,
        ReportStatus status,
        String createdAt,
        int totalItems
) {}
