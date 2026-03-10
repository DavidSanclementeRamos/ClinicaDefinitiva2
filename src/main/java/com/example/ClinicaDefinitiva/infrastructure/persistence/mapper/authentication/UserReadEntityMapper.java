package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.authentication;

import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityStatus;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication.UserIdentityEntity;

public class UserReadEntityMapper {

    // Mapea de UserEntity a UserIdentity
     public UserIdentity toDomain(UserIdentityEntity entity) {
        UserIdentity userIdentity;
         userIdentity = UserIdentity.reconstruct(
                 new UserIdentityId(entity.getId()),
                 Email.ofOrThrow(entity.getEmail()),
                 HashedPassword.of(entity.getHashedPassword()),
                 UserIdentityName.of(entity.getName()),
                 entity.getCreatedAt(),
                 entity.getLastLoginAt(),
                 entity.getFailedLoginAttempts(),
                 entity.getLockedUntil(),
                 entity.isVerified(),
                 UserIdentityStatus.of( UserIdentityStatus.Status.valueOf( entity.getStatus())),
                 entity.getVersion()
         );
       
        return userIdentity;
     }
}
