package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company;

import jakarta.validation.constraints.NotBlank;

public record UpdateCompanyContactRequest(
    @NotBlank String name,
    @NotBlank String legalRepresentative,
    @NotBlank String street,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank String country,
    @NotBlank String postalCode,
    @NotBlank String phoneNumber,
    @NotBlank String email
) {}
