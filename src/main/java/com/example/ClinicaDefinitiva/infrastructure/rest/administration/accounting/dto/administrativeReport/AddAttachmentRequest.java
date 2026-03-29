package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import jakarta.validation.constraints.NotBlank;

public record AddAttachmentRequest(
    @NotBlank String name,
    @NotBlank String url,
    @NotBlank String type,
    long size
) {}
