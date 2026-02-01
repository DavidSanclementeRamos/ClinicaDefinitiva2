package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity;

import com.example.ClinicaDefinitiva.application.dto.user.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityReadResponse;

public class UserServiceToRestMapper {

    // De DTO de aplicación de lectura → DTO REST de respuesta
    public UserIdentityReadResponse toRestDto(ReadUserIdentityDto dto) {
        return new UserIdentityReadResponse(
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
    public UserIdentityPageResponse toRestDto(PageUserIdentityDto dto) {
        return new UserIdentityPageResponse(
                dto.id(),
                dto.email(),
                dto.name(),
                dto.verified(),
                dto.status()
        );
    }
}

