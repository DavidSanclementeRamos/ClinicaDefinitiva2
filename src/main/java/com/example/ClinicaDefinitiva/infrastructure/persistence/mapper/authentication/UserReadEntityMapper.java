package com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.userIdentity;

import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication.UserIdentityEntity;

public class UserReadEntityMapper {

 /**   // Mapea de UserEntity a UserIdentity
    public UserIdentity toDomain(UserIdentityEntity entity) {
        if (entity == null) {
            return null;
        }
        Outcome<Email> emailOutcome = Email.of(entity.getEmail());

        Outcome<HashedPassword> passwordOutcome = HashedPassword.fromHash(entity.getHashedPassword());

        Outcome<UserIdentityName> userNameOutcome = UserIdentityName.create(entity.getName());


        return new UserIdentity(
                UserIdentityId.from(entity.getId()),
                 emailOutcome.getValue().get(),
                  passwordOutcome.getValue().get(),
                  userNameOutcome.getValue().get(),

                entity.getCreatedAt()

        );
    }*/
}
