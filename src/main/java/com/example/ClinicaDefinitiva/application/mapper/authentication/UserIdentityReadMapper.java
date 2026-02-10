package com.example.ClinicaDefinitiva.application.mapper.authentication;

import com.example.ClinicaDefinitiva.application.dto.authentication.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import org.springframework.stereotype.Component;

@Component
public class UserIdentityReadMapper {

    // dominio → DTO de lectura completo
    public ReadUserIdentityDto toReadDto(UserIdentity user) {
        return new ReadUserIdentityDto(
                user.getId().value(),
                user.getEmail().value(),
                user.getName().getValue(),
                user.getCreatedAt(),
                user.getLastLoginAt(),
                user.getFailedLoginAttempts(),
                user.getLockedUntil(),
                user.isVerified(),
                user.getStatus().toString(),
                user.getVersion()
        );
    }

    // dominio → DTO resumido (ej. para listados/paginación)
    public PageUserIdentityDto toPageDto(UserIdentity user) {
        return new PageUserIdentityDto(
                user.getId().value(),
                user.getEmail().value(),
                user.getName().getValue(),
                user.isVerified(),
                user.getStatus().toString()
        );
    }
}

