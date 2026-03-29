package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReadContractResponse(
    Long contractId,
    Long companyId,
    Long thirdPartyId,
    String name,
    String description,
    String origin,
    LocalDate startDate,
    LocalDate endDate,
    String coverageType,
    BigDecimal coverageRate,
    String status,
    //boolean isActiveAndValid,
    boolean isNearExpiration,
    long daysRemaining
) {}
