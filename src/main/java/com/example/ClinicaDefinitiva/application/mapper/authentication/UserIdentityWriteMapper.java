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

    public Outcome<Email> toEmail(CreateUserIdentityDto dto) {
        return Email.of(dto.email());
    }

    public Outcome<HashedPassword> toPassword(CreateUserIdentityDto dto) {
        return HashedPassword.fromHash(dto.password());
    }

    public Outcome<UserIdentityName> toUserName(CreateUserIdentityDto dto) {
        return UserIdentityName.create(dto.name());
    }

    public Outcome<Email> toEmail(UpdateUserIdentityDto dto) {
        return Email.of(dto.email());
    }

    public Outcome<HashedPassword> toPassword(UpdateUserIdentityDto dto) {
        return HashedPassword.fromHash(dto.password());
    }

    public Outcome<UserIdentityName> toUserName(UpdateUserIdentityDto dto) {
        return UserIdentityName.create(dto.name());
    }
}
   