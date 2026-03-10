package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.authentication;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication.UserIdentityEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityJpaRepository extends JpaRepository<UserIdentityEntity, Long > {
    Optional<UserIdentityEntity> findByEmail(String email );
    Optional <UserIdentityEntity> findByEmailAndStatus(String email, String status);
    Optional<UserIdentityEntity> findByIdAndStatus(Long id, String status);


}
