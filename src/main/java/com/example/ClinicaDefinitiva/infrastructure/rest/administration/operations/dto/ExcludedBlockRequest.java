// ExcludedBlockRequest.java
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ExcludedBlockRequest(
    @NotNull(message = "Block start time is required")
    LocalTime start,
    
    @NotNull(message = "Block end time is required")
    LocalTime end,
    
    @NotBlank(message = "Reason is required")
    String reason
) {}