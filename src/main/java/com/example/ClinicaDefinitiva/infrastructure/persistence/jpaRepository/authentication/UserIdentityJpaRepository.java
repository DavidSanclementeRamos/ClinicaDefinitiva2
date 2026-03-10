package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.authentication;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication.UserIdentityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityJpaRepository extends JpaRepository<UserIdentityEntity, Long > {
    Page<UserIdentityEntity> findByEmail(String email, Pageable pageable);
    Page<UserIdentityEntity> findByEmailAndStatus(String email, String status , Pageable pageable);
    Page<UserIdentityEntity> findByIdAndStatus(Long id, String status, Pageable pageable);


}
