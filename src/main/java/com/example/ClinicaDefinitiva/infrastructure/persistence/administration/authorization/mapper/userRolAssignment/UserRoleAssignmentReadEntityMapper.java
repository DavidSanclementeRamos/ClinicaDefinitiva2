package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.userRolAssignment;


import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.UserRoleAssignmentEntity;
import org.springframework.stereotype.Component;


import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;

@Component
public class UserRoleAssignmentReadEntityMapper {

    public UserRolAssignment toDomain(UserRoleAssignmentEntity entity) {
        
        UserRolAssignmentId id = UserRolAssignmentId.of(entity.getId());
        UserIdentityId userIdentityId = UserIdentityId.from(entity.getUserIdentity().getId());
        RolId rolId = RolId.of(entity.getRole().getId());
        
        return UserRolAssignment.reconstruct(
            id,
            userIdentityId,
            rolId,
            entity.getValidFrom(),
            entity.getValidUntil(),
            entity.isPrimary()
        );
    }
}
