package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.userIdentity;

import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.userIdentity.UserEntity;

public class UserReadEntityMapper {

    // Mapea de UserEntity a UserIdentity
    public UserIdentity toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserIdentity(
                UserIdentityId.from(entity.getId()),
                new Email(entity.getEmail()),
                new HashedPassword(entity.getHashedPassword()),
                new UserIdentityName(entity.getName()),
                entity.getCreatedAt()

        );
    }
}
