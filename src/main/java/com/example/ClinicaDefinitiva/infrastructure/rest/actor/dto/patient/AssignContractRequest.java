package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient;

import jakarta.validation.constraints.NotNull;

public record AssignContractRequest(
        @NotNull Long contractId
) {}
