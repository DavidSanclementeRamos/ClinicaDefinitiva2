package com.example.ClinicaDefinitiva.application.portsInput.userIdentity;

import com.example.ClinicaDefinitiva.application.dto.user.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.UpdateUserIdentityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserIdentityUseCase {

    ReadUserIdentityDto findById(Long id);
    Page<PageUserIdentityDto> findAll(Pageable pageable);
    Optional<PageUserIdentityDto> findByEmail(String email );
    Page<PageUserIdentityDto> findByEmailAndStatus(String email, String status, Pageable pageable);
    Page<PageUserIdentityDto> findByIdAndStatus(Long id, String status, Pageable pageable);
    ReadUserIdentityDto register(CreateUserIdentityDto dto);
    ReadUserIdentityDto recordSuccessfulLogin(Long userId);
    ReadUserIdentityDto recordFailedLogin(Long userId);
    ReadUserIdentityDto editUserData(UpdateUserIdentityDto dto, Long id);
    ReadUserIdentityDto verify(Long userId);
    ReadUserIdentityDto deactivate(Long userId, String reason);
    ReadUserIdentityDto suspend(Long userId, String reason);
    ReadUserIdentityDto canPerformSensitiveAction(Long userId);
    ReadUserIdentityDto authenticate(String email, String rawPassword);

}



