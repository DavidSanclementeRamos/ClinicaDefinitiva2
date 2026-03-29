package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.userRolAssignment;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.UserRoleAssignmentEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRoleAssignmentWriteEntityMapper {

    public UserRoleAssignmentEntity toEntity(UserRolAssignment domain) {
        UserRoleAssignmentEntity entity = new UserRoleAssignmentEntity();
        
        // ID
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        
        // Fechas
        entity.setValidFrom(domain.getValidFrom());
        entity.setValidUntil(domain.getValidTo());
        
        // Flags
        entity.setPrimary(domain.isPrimary());
        
        // Nota: userIdentity y role se setean en el adapter
        
        return entity;
    }
}