package com.example.ClinicaDefinitiva.domain.authentication;

import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserIdentityRepository {
    Optional<UserIdentity> findById(UserIdentityId id);
    Page<UserIdentity> findAll(Pageable pageable);
    Optional<UserIdentity> findByEmail(String email);
    UserIdentity save(UserIdentity user);

    Page<UserIdentity>   findByStatus(String status, Pageable pageable);


}
