package com.example.ClinicaDefinitiva.application.authentication.mapper;

import com.example.ClinicaDefinitiva.application.authentication.dto.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.ReadUserIdentityDto;
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
                user.getStatus().toString()
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

