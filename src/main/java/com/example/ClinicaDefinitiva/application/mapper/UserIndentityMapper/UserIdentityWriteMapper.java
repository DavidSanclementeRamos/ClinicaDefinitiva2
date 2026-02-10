package com.example.ClinicaDefinitiva.application.mapper.UserIndentityMapper;

import com.example.ClinicaDefinitiva.application.dto.user.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
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
                new UserIdentityName(dto.name()),
                Instant.now() // fecha de creación
        );
    }

    // DTO de actualización → dominio (aplica cambios sobre agregado existente)
    public void dtoUpdateToUserIdentity(UpdateUserIdentityDto dto, UserIdentity user) {
        user.editUserData(
                new UserIdentityName(dto.name()),

                new Email(dto.email()),
                new HashedPassword(dto.password()),
                Instant.now()
        );
    }
}

