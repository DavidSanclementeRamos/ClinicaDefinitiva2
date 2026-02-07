package com.example.ClinicaDefinitiva.application.service.adminitration.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.permission.UserRolAssignmentNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment.AssignmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment.AssignmentWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization.UserRolAssignmentUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
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

    public UserRolAssignmentApplicationService(UserRolAssignmentService userRolService,
                                               AssignmentWriteMapper writeMapper,
                                               AssignmentReadMapper readMapper,
                                               UserRolAssignmentRepository repository,
                                               AuthorizationService authorizationService,
                                               ReceptionRepository receptionRepository) {
        this.userRolService = userRolService;
        this.writeMapper = writeMapper;
        this.readMapper = readMapper;
        this.repository = repository;
        this.authorizationService = authorizationService;
        this.receptionRepository = receptionRepository;
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadAssignmentDto savePermanent(CreateAssignmentPermanentDto dto,
                                           UserId requesterId,
                                           RolId requesterRolId) {

        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(RolError.ERR_ROL_UNAUTHORIZED_CREATION, EntityContext.ASSIGNMENT);
        }

        UserRolAssignment assignment = writeMapper.fromCreatePermanent(dto);
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
                                           UserId requesterId,
                                           RolId requesterRolId) {


        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT), ActionCatalog.of(ActionCatalog.BasicAction.CREATE_TEMPORARY)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(RolError.ERR_ROL_UNAUTHORIZED_CREATION, EntityContext.ASSIGNMENT);
        }

        UserRolAssignment assignment = writeMapper.fromCreateTemporary(dto);
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
                              UserId requesterId,
                              RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),ActionCatalog.of(ActionCatalog.BasicAction.IS_ACTIVE_AT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targetId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(""));

        return assignment.isActiveAt(date);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)
    public boolean isCurrentlyActive(UserRolAssignmentId targetId,
                                     UserId requesterId,
                                     RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),ActionCatalog.of(ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targetId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(""));

        return assignment.isCurrentlyActive();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.EXTEND_ASSIGNMENT)
    public void extend(UserRolAssignmentId targetId,
                       LocalDate newValidTo,
                       UserId requesterId,
                       RolId requesterRolId) {

       Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT), ActionCatalog.of(ActionCatalog.BasicAction.EXTEND_ASSIGNMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targetId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_ASSIGNMENT_UNAUTHORIZED_EXTENSION,
                    VOContext.AUTHORIZATION
            );
        }

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(""));

        assignment.extend(newValidTo);
        repository.save(assignment);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action =  ActionCatalog.BasicAction.REVOKE_ALL)
    public void revokeAllRol(UserId targetUserId,
                             UserId requesterId,
                             RolId requesterRolId) {


        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT), ActionCatalog.of(ActionCatalog.BasicAction.REVOKE_ALL)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targetUserId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(AuthorizationError.ERR_ASSIGNMENT_UNAUTHORIZED_REVOKE, VOContext.AUTHORIZATION);
        }

        List<UserRolAssignment> assignments = repository.findByUserId(targetUserId);
        if (assignments.isEmpty()) {
            throw new UserRolAssignmentNotFoundException("");
        }
        userRolService.revokeAllRoles(targetUserId);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.REVOKE)
    public void revokeRol(UserId targetUserId,
                          RolId targetRolId,
                          UserId requesterId,
                          RolId requesterRolId) {


        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));


        SecurityContext context = SecurityContext
                .builder(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT), ActionCatalog.of(ActionCatalog.BasicAction.REVOKE)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targetRolId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(AuthorizationError.ERR_ASSIGNMENT_UNAUTHORIZED_REVOKE, VOContext.AUTHORIZATION);
        }

        List<UserRolAssignment> assignments = repository.findByUserIdAndRolId(targetUserId, targetRolId);
        if (assignments.isEmpty()) {
            throw new UserRolAssignmentNotFoundException("" + targetUserId + targetRolId);
        }

        userRolService.revokeRole(targetUserId,targetRolId);

    }



    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public Optional<ReadAssignmentDto> findById(UserRolAssignmentId targetId ,
                                                UserId requesterId,
                                                RolId requesterRolId) {


        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));


        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targetId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return repository.findById(targetId)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public List<ReadAssignmentDto> findByUserId(UserId targeUserId   ,
                                                    UserId requesterId,
                                                    RolId requesterRolId) {

        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));


        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targeUserId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return repository.findByUserId(targeUserId)
                .stream().map(readMapper::toReadDto).toList();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public Optional<ReadAssignmentDto> findByUserIdAndRolId(UserId targeUserId  , RolId  targeRolId,
                                                            UserId requesterId,
                                                            RolId requesterRolId) {


        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));


        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(targeRolId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return repository.findByUserIdAndRolId(UserId.from(targeRolId.getValue()), RolId.of(targeRolId.getValue()))
                .stream().map(readMapper::toReadDto).findFirst();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT,
            action = ActionCatalog.BasicAction.VIEW_ASSIGNMENT)
    public Optional<ReadAssignmentDto> findByUserIdAndIsPrimary(UserId targetUuserId,
                                                                boolean isPrimary,
                                                                UserId requesterId,
                                                                RolId requesterRolId) {


        Receptionist  receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

         Optional<UserRolAssignment> assignment = repository.findByUserIdAndIsPrimary(targetUuserId,isPrimary);

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(assignment.stream().count())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return repository.findByUserIdAndIsPrimary(targetUuserId, isPrimary)
                .map(readMapper::toReadDto);
    }


}
