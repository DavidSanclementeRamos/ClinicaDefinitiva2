package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.userIdentity;

import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.userIdentity.UserEntity;

public class UserWriteEntityMapper {
    // Mapea de UserIdentity a UserEntity
    public UserEntity toEntity(UserIdentity domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity(
                domain.getEmail().value(),
                domain.getHashedPassword().getHash(),
                domain.getName().getValue(),
                domain.getCreatedAt(),
                domain.isVerified(),
                domain.getStatus().toString()
        );

        // Ajustar campos adicionales
        // entity.s(domain.getId().getValue());
        entity.setLastLoginAt(domain.getLastLoginAt());
        entity.setFailedLoginAttempts(domain.getFailedLoginAttempts());
        entity.setLockedUntil(domain.getLockedUntil());
        //entity.setVersion(domain.getVersion());

        return entity;
    }

}

