package com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.mapper;


import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import org.springframework.stereotype.Component;

@Component
public class UserWriteEntityMapper {
    
    public UserIdentityEntity toEntity(UserIdentity domain) {
        if (domain == null) {
            return null;
        }

        // Crear entidad con los valores del dominio
        UserIdentityEntity entity = new UserIdentityEntity();
        
        // ID (solo si existe, para updates)
        if (domain.getId() != null && domain.getId().value() != null) {
            entity.setId(domain.getId().value());
        }
        
        // Datos básicos
        entity.setEmail(domain.getEmail().value());
        entity.setPasswordHash(domain.getHashedPassword().getHash());
        entity.setName(domain.getName().getValue());
        
        // Estado - convertir enum a String
        entity.setStatus(domain.getStatus().getValue().name());
        
        // Booleanos
        entity.setVerified(domain.isVerified());
        
        // Contadores y fechas
        entity.setFailedAttempts(domain.getFailedLoginAttempts());
        entity.setLockedUntil(domain.getLockedUntil());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setLastAccess(domain.getLastLoginAt());

        return entity;
    }
}