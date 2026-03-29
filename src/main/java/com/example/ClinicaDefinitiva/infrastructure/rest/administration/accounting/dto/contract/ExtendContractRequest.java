package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;

public record ExtendContractRequest(
    @NotNull @Future LocalDate newEndDate
) {}
