package com.example.ClinicaDefinitiva.application.dto.administration.accounting.thirdParties;

public record PageThirdPartyDto(
        Long id,
        String name,
        String documentNumber,
        String typeThirdParties,
        boolean active
) {}
