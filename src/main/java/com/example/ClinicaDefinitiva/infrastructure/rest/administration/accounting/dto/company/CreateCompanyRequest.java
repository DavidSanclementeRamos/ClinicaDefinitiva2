package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record CreateCompanyRequest(
    @NotBlank String name,
    @NotBlank @Pattern(regexp = "^\\d{5,12}(-\\d)?$") String taxIdentificationNumber,
    @NotBlank String typePerson,
    @NotBlank String taxRegime,
    @NotBlank String legalRepresentative,
    @NotBlank String street,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank String country,
    @NotBlank String postalCode,
    @NotBlank String phoneNumber,
    @NotBlank String email
   // @NotNull @Past LocalDate incorporationDate
) {}
