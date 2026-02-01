package com.example.ClinicaDefinitiva.domain.portsOutput;

import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.userIdentity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {
    Optional<UserIdentity> findById(UserId id);
    Page<UserIdentity> findAll(Pageable pageable);
    Optional<UserIdentity> findByEmail(String email);
    Page<UserIdentity> findByEmailAndStatus(String email, String status, Pageable pageable);
    Page<UserIdentity> findByIdAndStatus(Long id, String status, Pageable pageable);
    UserIdentity save(UserIdentity user);


}
