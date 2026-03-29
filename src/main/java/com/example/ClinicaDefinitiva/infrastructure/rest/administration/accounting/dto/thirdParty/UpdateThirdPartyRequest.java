package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty;

import jakarta.validation.constraints.NotBlank;

public record UpdateThirdPartyRequest(
    @NotBlank String name,
    String street,
    String city,
    String state,
    String country,
    String postalCode,
    String phoneNumber,
    String email
) {}