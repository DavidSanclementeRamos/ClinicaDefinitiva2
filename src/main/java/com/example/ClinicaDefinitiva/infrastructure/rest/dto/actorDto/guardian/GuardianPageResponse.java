package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian;

public record GuardianPageResponse (
        Long guardianId,
        // TypeGuardian
        String code,
        String description,

        String dni,
        String first,
        String lastName,
        String phoneNumber
){}
