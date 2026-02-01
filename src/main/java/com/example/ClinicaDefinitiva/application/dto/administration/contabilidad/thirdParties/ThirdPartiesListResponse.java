package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties;

public record ThirdPartiesListResponse(
        String id,
        NameDto name,
        String documentNumber,
        String typeThirdParties,
        boolean active
) {}
