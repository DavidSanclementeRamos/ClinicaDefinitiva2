package com.example.ClinicaDefinitiva.application.service.adminitration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.administration.permission.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.permission.UserRolAssignmentNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.UserIdentityNoFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment.AssignmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment.AssignmentWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization.UserRolAssignmentUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class UserRolAssignmentApplicationService implements UserRolAssignmentUseCase {

    private final UserRolAssignmentService userRolService;
    private final AssignmentWriteMapper writeMapper;
    private final AssignmentReadMapper readMapper;
    private final UserRolAssignmentRepository repository;
    private final AuthorizationService authorizationService;
    private final ReceptionRepository receptionRepository;
    private final AuthorizationHelper authorizationHelper;
    private final RolRepository rolRepository;
    private final UserIdentityRepository userIdentityRepository;

    public UserRolAssignmentApplicationService(UserRolAssignmentService userRolService, AssignmentWriteMapper writeMapper, AssignmentReadMapper readMapper, UserRolAssignmentRepository repository, AuthorizationService authorizationService, ReceptionRepository receptionRepository, AuthorizationHelper authorizationHelper, RolRepository rolRepository, UserIdentityRepository userIdentityRepository) {
        this.userRolService = userRolService;
        this.writeMapper = writeMapper;
        this.readMapper = readMapper;
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.receptionRepository = receptionRepository;
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


}
