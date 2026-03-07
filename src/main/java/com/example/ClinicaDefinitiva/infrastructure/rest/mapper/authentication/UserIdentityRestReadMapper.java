package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.authentication;

import com.example.ClinicaDefinitiva.application.dto.authentication.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.PageUserIdentityResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.ReadUserIdentityResponse;

public class UserIdentityRestReadMapper {

    // De DTO de aplicación de lectura → DTO REST de respuesta
    public ReadUserIdentityResponse toRestRead(ReadUserIdentityDto dto) {
        return new ReadUserIdentityResponse(
                dto.id(),
                dto.email(),
                dto.name(),
                dto.createdAt(),
                dto.lastLoginAt(),
                dto.failedLoginAttempts(),
                dto.lockedUntil(),
                dto.verified(),
                dto.status(),
                dto.version()
        );
    }

    // De DTO de aplicación de página → DTO REST de respuesta de página
    public PageUserIdentityResponse toRestPage(PageUserIdentityDto dto) {
        return new PageUserIdentityResponse(
                dto.id(),
                dto.email(),
                dto.name(),
                dto.verified(),
                dto.status()
        );
    }
}

