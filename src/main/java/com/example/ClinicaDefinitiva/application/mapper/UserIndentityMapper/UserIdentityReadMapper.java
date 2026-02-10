package com.example.ClinicaDefinitiva.application.mapper.UserIndentityMapper;

import com.example.ClinicaDefinitiva.application.dto.user.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import org.springframework.stereotype.Component;

@Component
public class UserIdentityReadMapper {

    // dominio → DTO de lectura completo
    public ReadUserIdentityDto toDto(UserIdentity user) {
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
    public PageUserIdentityDto pageToDto(UserIdentity user) {
        return new PageUserIdentityDto(
                user.getId().value(),
                user.getEmail().value(),
                user.getName().getValue(),
                user.isVerified(),
                user.getStatus().toString()
        );
    }
}

