package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

public record AddAttachmentToReportRequest(
        NameDto name,
        String url,
        String type,
        long size
) {}
