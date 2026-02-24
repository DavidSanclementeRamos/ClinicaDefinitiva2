package com.example.ClinicaDefinitiva.application.mapper.authentication;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserIdentityWriteMapper {

    // DTO de creación → dominio
    public UserIdentity fromCreateDto(CreateUserIdentityDto dto) {

        Outcome<Email> emailOutcome = Email.of(dto.email());

        Outcome<HashedPassword> passwordOutcome = HashedPassword.fromHash(dto.password());

        Outcome<UserIdentityName> userNameOutcome = UserIdentityName.create(dto.name());


        return UserIdentity.register(
                 // se genera nuevo ID
                emailOutcome.getValue().get(),
                passwordOutcome.getValue().get(),
                userNameOutcome.getValue().get(),
                Instant.now() // fecha de creación
        );
    }

    // DTO de actualización → dominio (aplica cambios sobre agregado existente)
    public void updateFromDto(UpdateUserIdentityDto dto, UserIdentity user) {
        Outcome<Email> emailOutcome = Email.of(dto.email());

        Outcome<HashedPassword> passwordOutcome = HashedPassword.fromHash(dto.password());

        Outcome<UserIdentityName> userNameOutcome = UserIdentityName.create(dto.name());

        user.update(
                userNameOutcome.getValue().get(),
                emailOutcome.getValue().get(),
                passwordOutcome.getValue().get(),
                Instant.now()
        );
    }
}

