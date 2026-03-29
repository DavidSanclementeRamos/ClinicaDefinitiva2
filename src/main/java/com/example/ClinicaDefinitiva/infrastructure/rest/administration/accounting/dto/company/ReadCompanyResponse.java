package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company;

import java.time.LocalDate;

public record ReadCompanyResponse(
    Long id,
    String name,
    String taxIdentificationNumber,
    String typePerson,
    String taxRegime,
    String legalRepresentative,
    String street,
    String city,
    String state,
    String country,
    String postalCode,
    String phoneNumber,
    String email,
    LocalDate incorporationDate,
    String status
) {}