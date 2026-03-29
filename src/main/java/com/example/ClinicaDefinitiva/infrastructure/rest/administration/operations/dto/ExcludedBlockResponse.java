// ExcludedBlockResponse.java
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto;

import java.time.LocalTime;

public record ExcludedBlockResponse(
    LocalTime start,
    LocalTime end,
    String reason
) {}
