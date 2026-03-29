package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company;

public record PageCompanyResponse(
    Long id,
    String name,
    String taxIdentificationNumber,
    String status
) {}
