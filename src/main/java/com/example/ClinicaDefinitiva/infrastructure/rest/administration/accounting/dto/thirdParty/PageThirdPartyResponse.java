package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty;

public record PageThirdPartyResponse(
    Long id,
    String name,
    String documentNumber,
    String thirdPartyType,
    boolean active
) {}