package com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.mapper;


import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityStatus;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import org.springframework.stereotype.Component;

@Component
public class UserReadEntityMapper {

    public UserIdentity toDomain(UserIdentityEntity entity) {
        // Crear Value Objects desde los valores de la entidad
        UserIdentityId id = UserIdentityId.from(entity.getId());
        
        // Email - asumiendo que tiene un método fromString o similar
        Email email = Email.ofOrThrow(entity.getEmail());
        
        // HashedPassword - usar of() que ya existe
        HashedPassword hashedPassword = HashedPassword.of(entity.getPasswordHash());
        
        // UserIdentityName - usar el constructor directamente o crear método of()
        UserIdentityName name =  UserIdentityName.of(entity.getName());
        
        // UserIdentityStatus - convertir String a Enum
        UserIdentityStatus.Status statusEnum = UserIdentityStatus.Status.valueOf(entity.getStatus());
        UserIdentityStatus status = UserIdentityStatus.of(statusEnum);
        
        return UserIdentity.reconstruct(
            id,
            email,
            hashedPassword,
            name,
            entity.getCreatedAt(),
            entity.getLastAccess(),
            entity.getFailedAttempts(),
            entity.getLockedUntil(),
            entity.isVerified(),
            status,
            entity.getVersion()
        );
    }
}