package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty;

public record ReadThirdPartyResponse(
    Long id,
    Long companyId,
    String name,
    String typeDocument,
    String documentNumber,
    String thirdPartyType,
    String street,
    String city,
    String state,
    String country,
    String postalCode,
    String phoneNumber,
    String email,
    boolean active
) {}