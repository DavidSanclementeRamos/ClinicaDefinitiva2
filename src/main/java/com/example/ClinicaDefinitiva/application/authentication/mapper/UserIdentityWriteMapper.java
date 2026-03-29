package com.example.ClinicaDefinitiva.application.authentication.mapper;

import com.example.ClinicaDefinitiva.application.authentication.dto.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserIdentityWriteMapper {

    public Email toEmail(CreateUserIdentityDto dto) {
        return Email.ofOrThrow(dto.email());
    }

    public HashedPassword toPassword(CreateUserIdentityDto dto) {
        return HashedPassword.of(dto.password());
    }

    public UserIdentityName toUserName(CreateUserIdentityDto dto) {
        return UserIdentityName.of(dto.name());
    }

   // UserIdentityWriteMapper.java
public Optional<UserIdentityName> toUserName(UpdateUserIdentityDto dto) {
    return Optional.ofNullable(dto.name())
            .map(UserIdentityName::of);
}

public Optional<Email> toEmail(UpdateUserIdentityDto dto) {
    return Optional.ofNullable(dto.email())
            .map(Email::ofOrThrow);
}

public Optional<HashedPassword> toPassword(UpdateUserIdentityDto dto) {
    return Optional.ofNullable(dto.password())
            .filter(pwd -> !pwd.isEmpty())
           // .map(passwordEncoder::encode)
            .map(HashedPassword::of);
}
}
   