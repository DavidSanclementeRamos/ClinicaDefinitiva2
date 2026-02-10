package com.example.ClinicaDefinitiva.application.service.adminitration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PermissionDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.ReceptionNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.permission.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol.RolReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol.RolWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization.RolUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.PermissionError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
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
    private final AuthorizationService authorizationService;
    private final ReceptionRepository receptionRepository;
    private final RolService rolService;


    public RolApplicationService(RolReadMapper readMapper, RolWriteMapper writeMapper, RolRepository repository, AuthorizationService authorizationService, ReceptionRepository receptionRepository, RolService rolService) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.receptionRepository = receptionRepository;
        this.rolService = rolService;
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Optional<ReadRolDto> findById(RolId targetRoleId  , UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Validar permiso base con @RequiresPermission ya lo hizo
        // 2. Buscar rol
        Optional<Rol> rolOpt = repository.findById(targetRoleId);

        if (rolOpt.isEmpty()) {
            return Optional.empty();
        }

        Rol rol = rolOpt.get();

        // 3. Validar autorización contextual (si el rol requiere validación adicional)
        // Para queries simples, @RequiresPermission es suficiente
        // Si necesitas validar ownership o contexto específico, hazlo aquí

        // 4. Mapear y retornar
        return Optional.of(readMapper.toReadDto(rol));
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Optional<ReadRolDto> findByRolEnum(String rolEnum, UserIdentityId requesterId, RolId requesterRolId) {
        return repository.findByRolEnum(RolEnum.valueOf(rolEnum))
                .map(readMapper::toReadDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Page<PageRolDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return repository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.VIEW_ROLE)
    @Override
    public Page<PageRolDto> findByEditable(boolean editable, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return repository.findByEditable(editable, pageable)
                .map(readMapper::toPageDto);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CREATE_CUSTOM_ROLE)
    @Override
    public ReadRolDto createCustom(CreateRolDto dto, UserIdentityId requesterId, RolId requesterRolId) {

        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE)), requesterId)
                .withSector(receptionist.getSector().Value()) // atributo contextual del recurso
                //.withResourceId(rol.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(RolError.ERR_ROL_UNAUTHORIZED_CREATION, EntityContext.ROL);
        }

        Rol rol = writeMapper.fromCreateDto(dto);

        Rol saved = rolService.createCustom(
                rol.getRolEnum(),
                rol.getDescription(),
                rol.getPermissions()
        );
        return readMapper.toReadDto(saved);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.CLONE_ROLE)
    @Override
    public ReadRolDto cloneRole(RolId sourceRolId, String newDescription,
                                UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Buscar rol fuente
        Rol sourceRol = repository.findById(sourceRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " + sourceRolId));

        // 2. Obtener receptionist para validación de sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
        if (receptionist == null) {
            throw new ReceptionNotFoundException(" not found  receptionist");
        }

        // 3. Construir contexto
        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                                ActionCatalog.of(ActionCatalog.BasicAction.CLONE_ROLE)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(sourceRol.getId().getValue())
                .build();

        // 4. Validar autorización
        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(RolError.ERR_ROL_UNAUTHORIZED_CLONE,EntityContext.ROL);
        }

        // 5. Clonar rol
        Rol clonedRol = rolService.cloneRole(sourceRol,newDescription);

        Rol saved = repository.save(clonedRol);

        return readMapper.toReadDto(saved);
    }
    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PERMISSION, action = ActionCatalog.BasicAction.ADD_PERMISSION)
    @Override
    public void addPermission(RolId targetRoleId , PermissionDto permissionDto,
                              UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Buscar rol
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " + targetRoleId));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
        if (receptionist == null) {
            throw new ReceptionNotFoundException("Requester is not a receptionist");
        }

        // 3. Construir contexto
        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.PERMISSION),
                                ActionCatalog.of(ActionCatalog.BasicAction.ADD_PERMISSION)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        // 4. Validar autorización
        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(PermissionError.ERR_PERMISSION_UNAUTHORIZED_ADD, VOContext.PERMISSION);
        }

        // 5. Agregar permiso
        Permission permission = writeMapper.toPermission(permissionDto);
        rol.addPermission(permission);

        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PERMISSION, action = ActionCatalog.BasicAction.REMOVE_PERMISSION)
    @Override
    public void removePermission(RolId targetRoleId , PermissionDto permissionDto,
                                 UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Buscar rol
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol not found: " ));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
        if (receptionist == null) {
            throw new ReceptionNotFoundException("Requester is not a receptionist");
        }

        // 3. Construir contexto
        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.PERMISSION),
                                ActionCatalog.of(ActionCatalog.BasicAction.REMOVE_PERMISSION)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        // 4. Validar autorización
        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(PermissionError.ERR_PERMISSION_UNAUTHORIZED_REMOVE,VOContext.PERMISSION);
        }

        // 5. Remover permiso
        Permission permission = writeMapper.toPermission(permissionDto);
        rol.removePermission(permission);

        repository.save(rol);
    }


    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PERMISSION, action = ActionCatalog.BasicAction.SET_PERMISSIONS)
    @Override
    public void setPermissions(RolId targetRoleId, Set<PermissionDto> permissionDtos,
                               UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Buscar rol
        Rol rol = repository.findById(requesterRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        if (receptionist == null) {
            throw new ReceptionNotFoundException("Requester is not a receptionist");
        }

        // 3. Construir contexto
        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.PERMISSION),
                                ActionCatalog.of(ActionCatalog.BasicAction.SET_PERMISSIONS)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        // 4. Validar autorización
        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(PermissionError.ERR_PERMISSION_UNAUTHORIZED_SET,VOContext.PERMISSION);
        }

        // 5. Reemplazar permisos
        Set<Permission> permissions = permissionDtos.stream()
                .map(writeMapper::toPermission)
                .collect(Collectors.toSet());

        rol.setPermissions(permissions);

        repository.save(rol);
    }


    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PERMISSION, action = ActionCatalog.BasicAction.CHECK_PERMISSION)
    @Override
    public boolean hasPermission(RolId targetRoleId, PermissionDto permissionDto,
                                 UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Buscar rol
        Rol rol = repository.findById(requesterRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
        if (receptionist == null) {
            throw new ReceptionNotFoundException("Requester is not a receptionist");
        }

        // 3. Construir contexto
        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.PERMISSION),
                                ActionCatalog.of(ActionCatalog.BasicAction.CHECK_PERMISSION)
                        ),
                        requesterId
                )
               .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        // 4. Validar autorización
        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(AuthorizationError.ERR_AUTH_PERMISSION_DENIED,VOContext.AUTHORIZATION);
        }

        // 5. Verificar permiso
        Permission permission = writeMapper.toPermission(permissionDto);
        return rol.hasPermission(permission);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.ACTIVATE_ROLE)
    public void activate(RolId targetRoleId, String reason,
                         UserIdentityId requesterId, RolId requesterRolId) {
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException(""));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                                ActionCatalog.of(ActionCatalog.BasicAction.ACTIVATE_ROLE)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        //authorizationService.checkPermission(context);

        rol.activate(reason);
        repository.save(rol);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.DEACTIVATE_ROLE)
    public void deactivate(RolId targetRoleId, String reason,
                           UserIdentityId requesterId, RolId requesterRolId) {
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException(""));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                                ActionCatalog.of(ActionCatalog.BasicAction.DEACTIVATE_ROLE)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        //authorizationService.checkPermission(context);

        rol.deactivate(reason);
        repository.save(rol);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.SUSPEND_ROLE)
    public void suspend(RolId targetRoleId, String reason,
                        UserIdentityId requesterId, RolId requesterRolId) {
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException(""));
        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                                ActionCatalog.of(ActionCatalog.BasicAction.SUSPEND_ROLE)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        //authorizationService.checkPermission(context);

        rol.suspend(reason);
        repository.save(rol);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE,
            action = ActionCatalog.BasicAction.MARK_DELETED_ROLE)
    public void markDeleted(RolId targetRoleId, String reason,
                            UserIdentityId requesterId, RolId requesterRolId) {
        Rol rol = repository.findById(targetRoleId)
                .orElseThrow(() -> new RolNotFoundException(""));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(
                        Permission.of(
                                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                                ActionCatalog.of(ActionCatalog.BasicAction.MARK_DELETED_ROLE)
                        ),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

       // authorizationService.checkPermission(context);

        rol.markDeleted(reason);
        repository.save(rol);
    }

    @Transactional
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ROLE, action = ActionCatalog.BasicAction.DELETE_ROLE)
    @Override
    public void deleteById(RolId targetRoleId, UserIdentityId requesterId, RolId requesterRolId) {
        // 1. Buscar rol
        Rol rol = repository.findById(requesterRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));

        // 2. Validar sector
        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));
        if (receptionist == null) {
            throw new ReceptionNotFoundException("Requester is not a receptionist");
        }

        // 3. Construir contexto
        SecurityContext context = SecurityContext
                .builder(
                        Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE)),
                        requesterId
                )
                .withSector(receptionist.getSector().Value())
                .withResourceId(rol.getId().getValue())
                .build();

        // 4. Validar autorización
        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(AuthorizationError.ERR_AUTH_ROLE_DELETE_UNAUTHORIZED,VOContext.AUTHORIZATION);
        }

        // 5. Eliminar (lógica de agregado)
        rol.delete(); // Marca como eliminado
        repository.save(rol);
    }
}


