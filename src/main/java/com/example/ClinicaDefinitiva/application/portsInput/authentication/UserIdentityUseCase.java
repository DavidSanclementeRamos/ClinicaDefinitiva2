package com.example.ClinicaDefinitiva.application.portsInput.authentication;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserIdentityUseCase {

    Optional<ReadUserIdentityDto> findById(UserIdentityId targetUserId, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageUserIdentityDto> findAll(Pageable pageable,UserIdentityId requesterId, RolId requesterRolId);
    Optional<PageUserIdentityDto> findByEmail(String email,UserIdentityId requesterId, RolId requesterRolId );
    Optional<PageUserIdentityDto> findByEmailAndStatus(String email, String status,UserIdentityId requesterId, RolId requesterRolId  );
    Optional<PageUserIdentityDto> findByIdAndStatus( UserIdentityId targetUserId, String status, UserIdentityId requesterId, RolId requesterRolId );


    ReadUserIdentityDto register(CreateUserIdentityDto dto,UserIdentityId requesterId, RolId requesterRolId);
    ReadUserIdentityDto update(UpdateUserIdentityDto dto, Long id,UserIdentityId requesterId, RolId requesterRolId);
    ReadUserIdentityDto deactivate( UserIdentityId targetUserId, String reason,UserIdentityId requesterId, RolId requesterRolId);
    ReadUserIdentityDto suspend( UserIdentityId targetUserId, String reason,UserIdentityId requesterId, RolId requesterRolId);
    ReadUserIdentityDto authenticate(String email, String rawPassword,UserIdentityId requesterId, RolId requesterRolId);
    ReadUserIdentityDto reactivate( UserIdentityId targetUserId, String reason,UserIdentityId requesterId, RolId requesterRolId);

}



