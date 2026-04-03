package com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIdentityJpaRepository extends JpaRepository<UserIdentityEntity, Long > {
    Optional<UserIdentityEntity> findByEmail(String email );
    Page<UserIdentityEntity>   findByStatus(String status, Pageable pageable);

}
