package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract;

import java.time.LocalDate;

public record PageContractResponse(
    Long contractId,
    String name,
    Long thirdPartyId,
    String thirdPartyName,
    LocalDate endDate,
    String status
) {}
