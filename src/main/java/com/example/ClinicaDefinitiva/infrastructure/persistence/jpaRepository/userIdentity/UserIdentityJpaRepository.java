package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.userIdentity;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.userIdentity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityJpaRepository extends JpaRepository<UserEntity, Long > {
    Page<UserEntity> findByEmail(String email, Pageable pageable);
    Page<UserEntity> findByEmailAndStatus(String email, String status ,Pageable pageable);
    Page<UserEntity> findByIdAndStatus(Long id, String status, Pageable pageable);


}
