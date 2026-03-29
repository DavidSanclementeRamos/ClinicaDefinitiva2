package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.mapper;

import com.example.ClinicaDefinitiva.application.authentication.dto.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.PageUserIdentityResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.ReadUserIdentityResponse;
import org.springframework.stereotype.Component;

@Component
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
                dto.status()
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

