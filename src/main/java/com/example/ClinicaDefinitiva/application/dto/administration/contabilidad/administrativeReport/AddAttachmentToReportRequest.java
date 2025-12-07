package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record AddAttachmentToReportRequest(
        NameDto name,
        String url,
        String type,
        long size
) {}
