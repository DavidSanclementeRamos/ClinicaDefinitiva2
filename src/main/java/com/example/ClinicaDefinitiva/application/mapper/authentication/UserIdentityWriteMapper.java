package com.example.ClinicaDefinitiva.application.mapper.authentication;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserIdentityWriteMapper {

    // DTO de creación → dominio
    public UserIdentity fromCreateDto(CreateUserIdentityDto dto) {
        return UserIdentity.register(
                 // se genera nuevo ID
                Email.of(dto.email()),
               HashedPassword.fromHash(dto.password()),
                new UserIdentityName(dto.name()),
                Instant.now() // fecha de creación
        );
    }

    // DTO de actualización → dominio (aplica cambios sobre agregado existente)
    public void updateFromDto(UpdateUserIdentityDto dto, UserIdentity user) {
        user.update(
                new UserIdentityName(dto.name()),

                new Email(dto.email()),
                new HashedPassword(dto.password()),
                Instant.now()
        );
    }
}

