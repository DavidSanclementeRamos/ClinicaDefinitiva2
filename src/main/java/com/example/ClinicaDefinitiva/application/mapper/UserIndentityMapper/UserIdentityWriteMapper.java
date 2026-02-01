package com.example.ClinicaDefinitiva.application.mapper.UserIndentityMapper;

import com.example.ClinicaDefinitiva.application.dto.user.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.HashedPassword;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserName;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserIdentityWriteMapper {

    // DTO de creación → dominio
    public UserIdentity dtoCreateToUserIdentity(CreateUserIdentityDto dto) {
        return UserIdentity.register(
                 // se genera nuevo ID
                new Email(dto.email()),
               new HashedPassword(dto.password()),
                new UserName(dto.name()),
                Instant.now() // fecha de creación
        );
    }

    // DTO de actualización → dominio (aplica cambios sobre agregado existente)
    public void dtoUpdateToUserIdentity(UpdateUserIdentityDto dto, UserIdentity user) {
        user.editUserData(
                new UserName(dto.name()),

                new Email(dto.email()),
                new HashedPassword(dto.password()),
                Instant.now()
        );
    }
}

