package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;
import com.example.ClinicaDefinitiva.domain.identity.valueObjectes.UserId;

import java.util.Optional;

public interface UserRepository {
    Optional<UserModel> findById(UserId id);
}
