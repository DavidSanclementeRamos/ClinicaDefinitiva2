package com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties;

public record PageThirdPartyDto(
        Long id,
        String name,
        String documentNumber,
        String typeThirdParties,
        boolean active
) {}
