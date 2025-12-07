package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record ThirdPartiesListResponse(
        String id,
        NameDto name,
        String documentNumber,
        String typeThirdParties,
        boolean active
) {}
