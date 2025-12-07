package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.identity.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.identity.valueObjectes.UserId;

import java.util.Optional;

public interface UserRepository {
    Optional<UserIdentity> findById(UserId id);
}
