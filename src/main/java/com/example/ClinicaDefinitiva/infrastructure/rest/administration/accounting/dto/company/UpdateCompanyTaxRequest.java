package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record UpdateCompanyTaxRequest(
    @NotBlank String taxIdentificationNumber,
    @NotBlank String taxRegime,
    @NotBlank String typePerson,
    @NotNull @Past LocalDate incorporationDate
) {}