package com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIdentityJpaRepository extends JpaRepository<UserIdentityEntity, Long > {
    Optional<UserIdentityEntity> findByEmail(String email );
    Optional <UserIdentityEntity> findByEmailAndStatus(String email, String status);
    Optional<UserIdentityEntity> findByIdAndStatus(Long id, String status);


}
