package com.example.ClinicaDefinitiva.domain.portsOutput;

import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;

import java.util.Optional;

public interface UserIdentityRepository {
    Optional<UserIdentity> findById(UserId id);

}
