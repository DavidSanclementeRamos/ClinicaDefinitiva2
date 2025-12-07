package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.Name;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ReportStatus;

public record ReportListResponse(
        String id,
        NameDto title,
        PeriodDto period,
        ReportStatus status,
        String createdAt,
        int totalItems
) {}
