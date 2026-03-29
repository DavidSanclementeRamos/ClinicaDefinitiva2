package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian;

public record PageGuardianResponse(
        Long guardianId,
        // TypeGuardian
        String code,
        String description,

        String dni,
        String first,
        String lastName,
        String phoneNumber
){}
