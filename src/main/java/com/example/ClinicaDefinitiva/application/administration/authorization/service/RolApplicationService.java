package com.example.ClinicaDefinitiva.application.administration.authorization.service;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.PermissionDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.input.RolUseCase;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.rol.RolReadMapper;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.rol.RolWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class RolApplicationService implements RolUseCase {

    private final RolReadMapper readMapper;
    private final RolWriteMapper writeMapper;
    private final RolRepository repository;
    private final AuthorizationHelper authorizationHelper;
    private final RolService rolService;

    public RolApplicationService(RolReadMapper readMapper, RolWriteMapper writeMapper,
                                  RolRepository repository, AuthorizationHelper authorizationHelper,
                                  RolService rolService) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
        this.rolService = rolService;
    }

    @Transactional(readOnly = true)
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.READ)
    @Override
    public ReadRolDto findById(RolId targetRoleId, UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        return readMapper.toReadDto(rol);
    }

    @Transactional(readOnly = true)
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.READ)
    @Override
    public ReadRolDto findByRolEnum(String rolEnum, UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );
           Rol rol = repository.findByRolEnum(RolEnum.valueOf(rolEnum)).orElseThrow(() -> new RolNotFoundException("Rol not found"));
              return  readMapper.toReadDto(rol);
    }

    @Transactional(readOnly = true)
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.READ)
    @Override
    public Page<PageRolDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );
        return repository.findAll(pageable).map(readMapper::toPageDto);
    }

    @Transactional(readOnly = true)
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.READ)
    @Override
    public Page<PageRolDto> findByEditable(boolean editable, Pageable pageable,
                                            UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );
        return repository.findByEditable(editable, pageable).map(readMapper::toPageDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CREATE_CUSTOM)
    @Override
    public ReadRolDto createCustom(CreateRolDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.CREATE_CUSTOM,
                AuthorizationContext.builder().build()
        );
        Rol created = rolService.createCustom(RolEnum.valueOf(dto.rolEnum()), dto.description());
        Rol saved = repository.save(created);
        return readMapper.toReadDto(saved);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CLONE)
    @Override
    public ReadRolDto cloneRole(RolId sourceRolId, String newDescription,
                                UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.CLONE,
                AuthorizationContext.builder().withResourceId(sourceRolId.getValue()).build()
        );
        Rol sourceRol = repository.findById(sourceRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " + sourceRolId));
        Rol clonedRol = rolService.cloneRole(sourceRol, newDescription);
        return readMapper.toReadDto(repository.save(clonedRol));
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.ADD)
    @Override
    public void addPermission(RolId targetRoleId, PermissionDto permissionDto,
                              UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.ADD,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.addPermission(writeMapper.toPermission(permissionDto));
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.REMOVE)
    @Override
    public void removePermission(RolId targetRoleId, PermissionDto permissionDto,
                                 UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.REMOVE,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.removePermission(writeMapper.toPermission(permissionDto));
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.SET_PERMISSIONS)
    @Override
    public void setPermissions(RolId targetRoleId, Set<PermissionDto> permissionDtos,
                               UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.SET_PERMISSIONS,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        // CORREGIDO: era repository.findById(requesterRolId) — buscaba el rol del que pide, no el objetivo
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        Set<Permission> permissions = permissionDtos.stream()
                .map(writeMapper::toPermission)
                .collect(Collectors.toSet());
        rol.setPermissions(permissions);
        repository.save(rol);
    }

    @Transactional(readOnly = true)
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CHECK)
    @Override
    public boolean hasPermission(RolId targetRoleId, PermissionDto permissionDto,
                                 UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.CHECK,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        // CORREGIDO: era repository.findById(requesterRolId) — buscaba el rol del que pide, no el objetivo
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        return rol.hasPermission(writeMapper.toPermission(permissionDto));
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.ACTIVATE)
    @Override
    public void activate(RolId targetRoleId, String reason,
                         UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.ACTIVATE,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.activate(reason);
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.DEACTIVATE )
    @Override
    public void deactivate(RolId targetRoleId, String reason,
                           UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.DEACTIVATE ,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.deactivate(reason);
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.SUSPEND )
    @Override
    public void suspend(RolId targetRoleId, String reason,
                        UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.SUSPEND ,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.suspend(reason);
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.MARK_DELETED )
    @Override
    public void markDeleted(RolId targetRoleId, String reason,
                            UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.MARK_DELETED ,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.markDeleted(reason);
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.DELETE)
    @Override
    public void deleteById(RolId targetRoleId, UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder().withResourceId(targetRoleId.getValue()).build()
        );
        // CORREGIDO: era repository.findById(requesterRolId) — buscaba el rol del que pide, no el objetivo
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found"));
        rol.delete();
        repository.delete(rol.getId());
    }
}