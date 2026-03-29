
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateOpeningBalanceRequest(
    @NotNull Long companyId,
    @NotNull Long accountId,
    Long thirdPartyId,
    @NotNull @Positive BigDecimal amount,
    @NotBlank String currency
) {}
