package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateThirdPartyRequest(
    @NotNull Long companyId,
    @NotBlank String name,
    @NotBlank String typeDocument,
    @NotBlank String documentNumber,
    @NotBlank String thirdPartyType,
    String street,
    String city,
    String state,
    String country,
    String postalCode,
    String phoneNumber,
    String email
) {}