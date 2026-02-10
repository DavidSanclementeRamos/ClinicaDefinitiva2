package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.authorization;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization.UserRolAssignmentEntity;

public class UserRolAssignmentEntityMapper {

    // Entity → Dominio
    public  UserRolAssignment toDomain(UserRolAssignmentEntity entity) {
        return new UserRolAssignment(
                UserRolAssignmentId.of(entity.getId()),   // Long → VO
                UserIdentityId.from(entity.getUserId()),            // Long → VO
                RolId.of(entity.getRolId()),              // Long → VO
                entity.getValidFrom(),
                entity.getValidTo(),
                entity.isPrimary()
        );
    }

    // Dominio → Entity
    public  UserRolAssignmentEntity toEntity(UserRolAssignment domain) {
        UserRolAssignmentEntity entity = new UserRolAssignmentEntity();
        entity.setId(domain.getId() != null ? domain.getId().getValue() : null); // VO → Long
        entity.setUserId(domain.getUserId().value());                         // VO → Long
        entity.setRolId(domain.getRolId().getValue());                           // VO → Long
        entity.setValidFrom(domain.getValidFrom());
        entity.setValidTo(domain.getValidTo());
        entity.setPrimary(domain.isPrimary());
        return entity;
    }
}

