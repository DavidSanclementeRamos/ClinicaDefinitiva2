package com.example.ClinicaDefinitiva.application.service.administration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PermissionDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol.RolReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol.RolWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization.RolUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
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

@Service
public class RolApplicationService implements RolUseCase {
    private final RolReadMapper readMapper;
    private final RolWriteMapper writeMapper;
    private final RolRepository repository;
    private final AuthorizationHelper authorizationHelper;
    private final RolService rolService;

    public RolApplicationService(RolReadMapper readMapper, RolWriteMapper writeMapper, RolRepository repository, AuthorizationHelper authorizationHelper, RolService rolService) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
        this.rolService = rolService;
    }


    

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Optional<ReadRolDto> findById(RolId targetRoleId  , UserIdentityId requesterId, RolId requesterRolId) {
        

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.VIEW_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        
         Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));
        return Optional.of(readMapper.toReadDto(rol));
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Optional<ReadRolDto> findByRolEnum(String rolEnum, UserIdentityId requesterId, RolId requesterRolId) {
      
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.VIEW_ROLE,
                AuthorizationContext.builder()
                        .build()
        );
        
        return repository.findByRolEnum(RolEnum.valueOf(rolEnum))
                .map(readMapper::toReadDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Page<PageRolDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
       
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.VIEW_ROLE,
                AuthorizationContext.builder()
                        .build()
        );
        return repository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Page<PageRolDto> findByEditable(boolean editable, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
       
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.VIEW_ROLE,
                AuthorizationContext.builder()
                        .build()
        );
        return repository.findByEditable(editable, pageable)
                .map(readMapper::toPageDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CREATE_CUSTOM_ROLE)
    @Override
    public ReadRolDto createCustom(CreateRolDto dto, UserIdentityId requesterId, RolId requesterRolId) {

       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.CREATE_CUSTOM_ROLE,
                AuthorizationContext.builder()
                        .build()
        );

        Rol create = rolService.createCustom(
              RolEnum.valueOf(  dto.rolEnum()),
                dto.description()
                
        );
        
        Rol saved = repository.save(create);
        return readMapper.toReadDto(saved);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CLONE_ROLE)
    @Override
    public ReadRolDto cloneRole(RolId sourceRolId, String newDescription,
                                UserIdentityId requesterId, RolId requesterRolId) {
        
       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.CLONE_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(sourceRolId.getValue())
                        .build()
        );
        Rol sourceRol = repository.findById(sourceRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " + sourceRolId));


        Rol clonedRol = rolService.cloneRole(sourceRol,newDescription);

        Rol saved = repository.save(clonedRol);

        return readMapper.toReadDto(saved);
    }
    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.ADD_PERMISSION)
    @Override
    public void addPermission(RolId targetRoleId , PermissionDto permissionDto,
                              UserIdentityId requesterId, RolId requesterRolId) {
       
       
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.ADD_PERMISSION,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " + targetRoleId));


        //  Agregar permiso
        Permission permission = writeMapper.toPermission(permissionDto);
        rol.addPermission(permission);

        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.REMOVE_PERMISSION)
    @Override
    public void removePermission(RolId targetRoleId , PermissionDto permissionDto,
                                 UserIdentityId requesterId, RolId requesterRolId) {


     authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.REMOVE_PERMISSION,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );

        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol not found: " ));

     
        
        //  Remover permiso
        Permission permission = writeMapper.toPermission(permissionDto);
        rol.removePermission(permission);

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
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );

        Rol rol = repository.findById(requesterRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));

       
      
        // Reemplazar permisos
        Set<Permission> permissions = permissionDtos.stream()
                .map(writeMapper::toPermission)
           .collect(Collectors.toSet());

         rol.setPermissions(permissions);

        repository.save(rol);
    }


    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CHECK_PERMISSION)
    @Override
    public boolean hasPermission(RolId targetRoleId, PermissionDto permissionDto,
                                 UserIdentityId requesterId, RolId requesterRolId) {


          authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.CHECK_PERMISSION,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );

        Rol rol = repository.findById(requesterRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));

       

        // 5. Verificar permiso
        Permission permission = writeMapper.toPermission(permissionDto);
        return rol.hasPermission(permission);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.ACTIVATE_ROLE)
    public void activate(RolId targetRoleId, String reason,
                         UserIdentityId requesterId, RolId requesterRolId) {
      
          authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.ACTIVATE_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException(""));

      
        rol.activate(reason);
        repository.save(rol);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.DEACTIVATE_ROLE)
    public void deactivate(RolId targetRoleId, String reason,
                           UserIdentityId requesterId, RolId requesterRolId) {
       
          authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.DEACTIVATE_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException(""));

       
        rol.deactivate(reason);
        repository.save(rol);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.SUSPEND_ROLE)
    public void suspend(RolId targetRoleId, String reason,
                        UserIdentityId requesterId, RolId requesterRolId) {
      
          authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.SUSPEND_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Not found"));
       

        rol.suspend(reason);
        repository.save(rol);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.MARK_DELETED_ROLE)
    public void markDeleted(RolId targetRoleId, String reason,
                            UserIdentityId requesterId, RolId requesterRolId) {
      
          authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.MARK_DELETED_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Not found"));

       
        rol.markDeleted(reason);
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.DELETE_ROLE)
    @Override
    public void deleteById(RolId targetRoleId, UserIdentityId requesterId, RolId requesterRolId) {
       
        
        
          authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ROLE,
                ActionCatalog.BasicAction.DELETE_ROLE,
                AuthorizationContext.builder()
                        .withResourceId(targetRoleId.getValue())
                        .build()
        );
        
          Rol rol = repository.findById(requesterRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));

   
        rol.delete(); 
        repository.delete(rol.getId());
    }
}


