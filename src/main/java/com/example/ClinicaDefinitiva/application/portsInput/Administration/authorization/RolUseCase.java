package com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PermissionDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

/**
 * Casos de uso para gestión de roles
 * Todos reciben UserId y RolId explícitamente para autorización
 */
public interface RolUseCase {


    Optional<ReadRolDto> findById(RolId targetRoleId  , UserId requesterId, RolId requesterRolId);
    Optional<ReadRolDto> findByRolEnum(String rolEnum, UserId requesterId, RolId requesterRolId);
    Page<PageRolDto> findAll(Pageable pageable, UserId requesterId, RolId requesterRolId);
    Page<PageRolDto> findByEditable(boolean editable, Pageable pageable,
                                    UserId requesterId, RolId requesterRolId);

    ReadRolDto createCustom(CreateRolDto dto, UserId requesterId, RolId requesterRolId);
    ReadRolDto cloneRole( RolId targetRoleId, String newDescription,
                         UserId requesterId, RolId requesterRolId);

    void addPermission(RolId targetRoleId , PermissionDto permissionDto,
                       UserId requesterId, RolId requesterRolId);

    void removePermission(RolId targetRoleId, PermissionDto permission,
                          UserId requesterId, RolId requesterRolId);

    void setPermissions(RolId targetRoleId, Set<PermissionDto> permissions,
                        UserId requesterId, RolId requesterRolId);

    boolean hasPermission(RolId targetRoleId, PermissionDto permission,
                          UserId requesterId, RolId requesterRolId);

    void activate(RolId targetRoleId, String reason, UserId requesterId, RolId requesterRolId);
    void deactivate(RolId targetRoleId, String reason, UserId requesterId, RolId requesterRolId);

    void suspend(RolId targetRoleId, String reason, UserId requesterId, RolId requesterRolId);
    void markDeleted(RolId targetRoleId, String reason, UserId requesterId, RolId requesterRolId);

    void deleteById(RolId targetRoleId, UserId requesterId, RolId requesterRolId);
}

