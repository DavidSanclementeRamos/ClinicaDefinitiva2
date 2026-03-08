package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties;

public record PageThirdPartyDto(
        Long id,
        String name,
        String documentNumber,
        String typeThirdParties,
        boolean active
) {}
