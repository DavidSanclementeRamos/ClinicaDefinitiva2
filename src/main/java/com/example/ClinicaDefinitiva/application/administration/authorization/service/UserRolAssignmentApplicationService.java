package com.example.ClinicaDefinitiva.application.administration.authorization.service;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.input.UserRolAssignmentUseCase;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.userRolAssignment.AssignmentReadMapper;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.userRolAssignment.AssignmentWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.UserRolAssignmentNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.authentication.UserIdentityNoFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.UserRolAssignmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UserRolAssignmentApplicationService implements UserRolAssignmentUseCase {

    private final UserRolAssignmentService userRolService;
    private final AssignmentWriteMapper writeMapper;
    private final AssignmentReadMapper readMapper;
    private final UserRolAssignmentRepository repository;
    private final AuthorizationHelper authorizationHelper;
    private final RolRepository rolRepository;
    private final UserIdentityRepository userIdentityRepository;

    public UserRolAssignmentApplicationService(UserRolAssignmentService userRolService, AssignmentWriteMapper writeMapper, AssignmentReadMapper readMapper, UserRolAssignmentRepository repository, AuthorizationHelper authorizationHelper, RolRepository rolRepository, UserIdentityRepository userIdentityRepository) {
        this.userRolService = userRolService;
        this.writeMapper = writeMapper;
        this.readMapper = readMapper;
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
        this.rolRepository = rolRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

   



   


    

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadAssignmentDto savePermanent(CreateAssignmentPermanentDto dto,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

       
        
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder()
                        .build()
        );

        UserRolAssignment assignment = UserRolAssignment.assignPermanent(
            writeMapper.toUserIdentityId(dto),
            writeMapper.toRolId(dto),
            writeMapper.toIsPrimary(dto)
        );

        UserRolAssignment saved = userRolService.assignRole(
                assignment.getUserId(),
                assignment.getRolId(),
                assignment.isPrimary()
        );
        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.CREATE_TEMPORARY)
    public ReadAssignmentDto saveTemporary(CreateAssignmentTemporaryDto dto,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {


       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.CREATE_TEMPORARY,
                AuthorizationContext.builder()
                        .build()
        );

         UserRolAssignment assignment = UserRolAssignment.assignTemporary(
            writeMapper.toUserIdentityId(dto),
            writeMapper.toRolId(dto),
            writeMapper.toValidFrom(dto),
            writeMapper.toValidTo(dto),
            writeMapper.toIsPrimary(dto)
        );

        UserRolAssignment saved = userRolService.assignTemporaryRole(
                assignment.getUserId(),
                assignment.getRolId(),
                assignment.getValidFrom(),
                assignment.getValidTo(),
                assignment.isPrimary()
        );
        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.IS_ACTIVE_AT)
    public boolean isActiveAt(UserRolAssignmentId targetId,
                              LocalDate date,
                              UserIdentityId requesterId,
                              RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.IS_ACTIVE_AT,
                AuthorizationContext.builder()
                        .withResourceId(targetId.getValue())
                        .build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Not fount"));

        return assignment.isActiveAt(date);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)
    public boolean isCurrentlyActive(UserRolAssignmentId targetId,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE,
                AuthorizationContext.builder()
                        .withResourceId(targetId.getValue())
                        .build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Not fount"));

        return assignment.isCurrentlyActive();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.EXTEND_ASSIGNMENT)
    public void extend(UserRolAssignmentId targetId,
                       LocalDate newValidTo,
                       UserIdentityId requesterId,
                       RolId requesterRolId) {

       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.EXTEND_ASSIGNMENT,
                AuthorizationContext.builder()
                        .withResourceId(targetId.getValue())
                        .build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Not fount"));

        assignment.extend(newValidTo);
        repository.save(assignment);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action =  ActionCatalog.BasicAction.REVOKE_ALL)
    public void revokeAllRol(UserIdentityId targetUserIdentityId,
                             UserIdentityId requesterId,
                             RolId requesterRolId) {


       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.REVOKE_ALL,
                AuthorizationContext.builder()
                        .withResourceId(targetUserIdentityId.value())
                        .build()
        );

        List<UserRolAssignment> assignments = repository.findByUserId(targetUserIdentityId);
        if (assignments.isEmpty()) {
            throw new UserRolAssignmentNotFoundException("Not fount");
        }
        userRolService.revokeAllRoles(targetUserIdentityId);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.REVOKE)
    public void revokeRol(UserIdentityId targetUserIdentityId,
                          RolId targetRolId,
                          UserIdentityId requesterId,
                          RolId requesterRolId) {
        
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.REVOKE,
                AuthorizationContext.builder()
                        .withResourceId(targetUserIdentityId.value())
                        .build()
        );
         
          UserIdentity userIdentity = userIdentityRepository.findById(targetUserIdentityId)
                .orElseThrow(() -> new UserIdentityNoFoundException("Not found"));
          
           Rol rol = rolRepository.findById(targetRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));
                        
   
     
        userRolService.revokeRole(userIdentity.getId(),rol.getId());

    }



    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public Optional<ReadAssignmentDto> findById(UserRolAssignmentId targetId ,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {


        
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.VIEW_ASSIGNMENT,
                AuthorizationContext.builder()
                        .withResourceId(targetId.getValue())
                        .build()
        );
         
          UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Not found"));


        return repository.findById(assignment.getId())
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public List<ReadAssignmentDto> findByUserId(UserIdentityId targeUserIdentityId,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.VIEW_ASSIGNMENT,
                AuthorizationContext.builder()
                        .build()
        );
        
         UserIdentity userIdentity = userIdentityRepository.findById(targeUserIdentityId)
                .orElseThrow(() -> new UserIdentityNoFoundException("Not found"));
          
         

        return repository.findByUserId(userIdentity.getId())
                .stream().map(readMapper::toReadDto).toList();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public Optional<ReadAssignmentDto> findByUserIdAndRolId(UserIdentityId targeUserIdentityId, RolId  targeRolId,
                                                            UserIdentityId requesterId,
                                                            RolId requesterRolId) {


        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.VIEW_ASSIGNMENT,
                AuthorizationContext.builder()
                        .build()
        );
        
         UserIdentity userIdentity = userIdentityRepository.findById(targeUserIdentityId)
                .orElseThrow(() -> new UserIdentityNoFoundException("Not found"));
          
          Rol rol = rolRepository.findById(targeRolId)
                .orElseThrow(() -> new RolNotFoundException("Rol not found: " ));
                       

        return repository.findByUserIdAndRolId(userIdentity.getId(), rol.getId())
                .stream().map(readMapper::toReadDto).findFirst();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public Optional<ReadAssignmentDto> findByUserIdAndIsPrimary(UserIdentityId targetUserIdentityId,
                                                                boolean isPrimary,
                                                                UserIdentityId requesterId,
                                                                RolId requesterRolId) {


    
         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.VIEW_ASSIGNMENT,
                AuthorizationContext.builder()
                        .build()
        );
        
         UserIdentity userIdentity = userIdentityRepository.findById(targetUserIdentityId)
                .orElseThrow(() -> new UserIdentityNoFoundException("Not found"));
          


        return repository.findByUserIdAndIsPrimary(userIdentity.getId(), isPrimary)
                .map(readMapper::toReadDto);
    }

   
    
    
    
    
    
    

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.UPDATE_PRIMARY)
    public ReadAssignmentDto updatePrimary(UserRolAssignmentId targetId, 
                                           boolean isPrimary,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {
        
        // 1. Autorización
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.UPDATE_PRIMARY,
                AuthorizationContext.builder()
                        .withResourceId(targetId.getValue())
                        .build()
        );

        // 2. Buscar la asignación
        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(
                        "Assignment not found with id: " + targetId.getValue()));

        // 3. Validar reglas de negocio
        if (assignment.isPrimary() == isPrimary) {
            // Ya está en el estado deseado, no hacer nada
            return readMapper.toReadDto(assignment);
        }

        // 4. Si vamos a marcar como primario, quitar primary de otros roles
        if (isPrimary) {
            removePrimaryFromOtherRoles(assignment.getUserId());
        }

        // 5. Actualizar usando el método del repositorio (updatePrimary)
        repository.updatePrimary(targetId, isPrimary);

        // 6. Retornar la asignación actualizada
        return repository.findById(targetId)
                .map(readMapper::toReadDto)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(
                        "Assignment not found after update: " + targetId.getValue()));
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteAssignment(UserRolAssignmentId targetId,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {
        
        // 1. Autorización
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.REVOKE_ASSIGNMENT,
                AuthorizationContext.builder()
                        .withResourceId(targetId.getValue())
                        .build()
        );

        // 2. Buscar la asignación para verificar que existe
        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(
                        "Assignment not found with id: " + targetId.getValue()));

        // 3. Validar que no sea el último rol activo
        List<UserRolAssignment> activeAssignments = repository
                .findByUserId(assignment.getUserId())
                .stream()
                .filter(UserRolAssignment::isCurrentlyActive)
                .toList();

        if (activeAssignments.size() == 1 && 
            activeAssignments.get(0).getId().equals(targetId)) {
            throw new BusinessRuleViolationException(UserRolAssignmentError.valueOf(
                    "Cannot delete the last active role of a user"),
                    EntityContext.ASSIGNMENT
            );
        }

        // 4. Si es rol primario, necesitamos elegir otro como primario
        if (assignment.isPrimary() && assignment.isCurrentlyActive()) {
            // Buscar otro rol activo para hacerlo primario
            UserRolAssignment newPrimary = activeAssignments.stream()
                    .filter(a -> !a.getId().equals(targetId))
                    .findFirst()
                    .orElseThrow(() -> new BusinessRuleViolationException(UserRolAssignmentError.valueOf(
                            "Cannot delete primary role without another active role to replace it"),
                            EntityContext.ASSIGNMENT
                    ));
            
            // Marcar el nuevo rol como primario
            repository.updatePrimary(newPrimary.getId(), true);
        }

        // 5. Eliminar (revocar) la asignación
        repository.delete(targetId);
    }

    // Método auxiliar para quitar primary de otros roles
    private void removePrimaryFromOtherRoles(UserIdentityId userId) {
        repository.findByUserIdAndIsPrimary(userId, true)
                .ifPresent(currentPrimary -> 
                    repository.updatePrimary(currentPrimary.getId(), false)
                );
    }

}
