package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.authentication;

import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication.UserIdentityEntity;

public class UserWriteEntityMapper {
    // Mapea de UserIdentity a UserEntity
    public UserIdentityEntity toEntity(UserIdentity domain) {
        if (domain == null) {
            return null;
        }

        UserIdentityEntity entity = new UserIdentityEntity(
                domain.getEmail().value(),
                domain.getHashedPassword().getHash(),
                domain.getName().getValue(),
                domain.getCreatedAt(),
                domain.isVerified(),
                domain.getStatus().toString()
        );

        // Ajustar campos adicionales
         entity.setId(domain.getId().value());
        entity.setLastLoginAt(domain.getLastLoginAt());
        entity.setFailedLoginAttempts(domain.getFailedLoginAttempts());
        entity.setLockedUntil(domain.getLockedUntil());
        entity.setVersion(domain.getVersion());

        return entity;
    }

}

