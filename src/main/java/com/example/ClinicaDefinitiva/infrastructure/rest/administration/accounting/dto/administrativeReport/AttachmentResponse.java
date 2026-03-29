
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

public record AttachmentResponse(
    String name,
    String url,
    String type,
    long size
) {}