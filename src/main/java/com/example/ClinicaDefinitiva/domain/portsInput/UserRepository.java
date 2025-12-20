package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserIdentity> findById(UserId id);
}
