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
    Optional<UserIdentity> findByEmailAndStatus(String email, String status);
    Optional<UserIdentity> findByIdAndStatus(UserIdentityId id, String status);
    UserIdentity save(UserIdentity user);


}
