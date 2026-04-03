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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public UserRolAssignmentApplicationService(UserRolAssignmentService userRolService,
                                               AssignmentWriteMapper writeMapper,
                                               AssignmentReadMapper readMapper,
                                               UserRolAssignmentRepository repository,
                                               AuthorizationHelper authorizationHelper,
                                               RolRepository rolRepository,
                                               UserIdentityRepository userIdentityRepository) {
        this.userRolService = userRolService;
        this.writeMapper = writeMapper;
        this.readMapper = readMapper;
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
        this.rolRepository = rolRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.CREATE)
    public ReadAssignmentDto savePermanent(CreateAssignmentPermanentDto dto,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
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
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.CREATE_TEMPORARY)
    public ReadAssignmentDto saveTemporary(CreateAssignmentTemporaryDto dto,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.CREATE_TEMPORARY,
                AuthorizationContext.builder().build()
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
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.IS_ACTIVE_AT)
    public boolean isActiveAt(UserRolAssignmentId targetId, LocalDate date,
                              UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.IS_ACTIVE_AT,
                AuthorizationContext.builder().withResourceId(targetId.getValue()).build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found: " + targetId.getValue()));
        return assignment.isActiveAt(date);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)
    public boolean isCurrentlyActive(UserRolAssignmentId targetId,
                                     UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE,
                AuthorizationContext.builder().withResourceId(targetId.getValue()).build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found: " + targetId.getValue()));
        return assignment.isCurrentlyActive();
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.READ)
    public void extend(UserRolAssignmentId targetId, LocalDate newValidTo,
                       UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().withResourceId(targetId.getValue()).build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found: " + targetId.getValue()));
        assignment.extend(newValidTo);
        repository.save(assignment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.REVOKE_ALL)
    public void revokeAllRol(UserIdentityId targetUserIdentityId,
                             UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.REVOKE_ALL,
                AuthorizationContext.builder().withResourceId(targetUserIdentityId.value()).build()
        );

        userRolService.revokeAllRoles(targetUserIdentityId);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.REVOKE)
    public void revokeRol(UserIdentityId targetUserIdentityId, RolId targetRolId,
                          UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.REVOKE,
                AuthorizationContext.builder().withResourceId(targetUserIdentityId.value()).build()
        );

        userRolService.revokeRole(targetUserIdentityId, targetRolId);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.READ)
    public ReadAssignmentDto findById(UserRolAssignmentId targetId,
                                      UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().withResourceId(targetId.getValue()).build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found: " + targetId.getValue()));
        return readMapper.toReadDto(assignment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.READ)
    public Page<ReadAssignmentDto> findByUserId(UserIdentityId targetUserIdentityId, 
                                                UserIdentityId requesterId, RolId requesterRolId, Pageable pageable) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByUserId(targetUserIdentityId, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.READ)
    public Page<ReadAssignmentDto> findByUserIdAndRolId(UserIdentityId targetUserIdentityId, RolId targetRolId,
                                                        UserIdentityId requesterId, RolId requesterRolId, Pageable pageable) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByUserIdAndRolId(targetUserIdentityId, targetRolId, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.READ)
    public ReadAssignmentDto findByUserIdAndIsPrimary(UserIdentityId targetUserIdentityId, boolean isPrimary,
                                                      UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        UserRolAssignment assignment = repository.findByUserIdAndIsPrimary(targetUserIdentityId, isPrimary)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException(
                        "No assignment found for user " + targetUserIdentityId.value() + " with isPrimary=" + isPrimary));
        return readMapper.toReadDto(assignment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.UPDATE_PRIMARY)
    public ReadAssignmentDto updatePrimary(UserRolAssignmentId targetId, boolean isPrimary,
                                           UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ASSIGNMENT,
                ActionCatalog.BasicAction.UPDATE_PRIMARY,
                AuthorizationContext.builder().withResourceId(targetId.getValue()).build()
        );

        UserRolAssignment assignment = repository.findById(targetId)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found: " + targetId.getValue()));

        if (assignment.isPrimary() == isPrimary) {
            return readMapper.toReadDto(assignment);
        }

        if (isPrimary) {
            removePrimaryFromOtherRoles(assignment.getUserId());
        }

        repository.updatePrimary(targetId, isPrimary);

        return repository.findById(targetId)
                .map(readMapper::toReadDto)
                .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found after update: " + targetId.getValue()));
    }


 
@Override
@RequiresPermission(resource = ResourceCatalog.BasicResource.ASSIGNMENT, action = ActionCatalog.BasicAction.DELETE)
public void deleteAssignment(UserRolAssignmentId targetId,
                             UserIdentityId requesterId, RolId requesterRolId) {
    authorizationHelper.authorize(
            requesterId, requesterRolId,
            ResourceCatalog.BasicResource.ASSIGNMENT,
            ActionCatalog.BasicAction.DELETE,
            AuthorizationContext.builder().withResourceId(targetId.getValue()).build()
    );

    UserRolAssignment assignment = repository.findById(targetId)
            .orElseThrow(() -> new UserRolAssignmentNotFoundException("Assignment not found: " + targetId.getValue()));

    // Obtener todas las asignaciones del usuario (sin paginación)
    Page<UserRolAssignment> allAssignmentsPage = repository.findByUserId(assignment.getUserId(), Pageable.unpaged());
    List<UserRolAssignment> activeAssignments = allAssignmentsPage.getContent().stream()
            .filter(UserRolAssignment::isCurrentlyActive)
            .collect(Collectors.toList());

    // Validar que no sea el último rol activo
    if (activeAssignments.size() == 1 && activeAssignments.get(0).getId().equals(targetId)) {
        throw new BusinessRuleViolationException(
                UserRolAssignmentError.ERR_ASSIGNMENT_CANNOT_REVOKE_LAST_INDIVIDUAL,
                EntityContext.ASSIGNMENT
        );
    }

    // Si es rol primario, elegir otro como primario
    if (assignment.isPrimary() && assignment.isCurrentlyActive()) {
        UserRolAssignment newPrimary = activeAssignments.stream()
                .filter(a -> !a.getId().equals(targetId))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserRolAssignmentError.ERR_ASSIGNMENT_NO_OTHER_ACTIVE_ROLE,
                        EntityContext.ASSIGNMENT
                ));
        repository.updatePrimary(newPrimary.getId(), true);
    }

    // Eliminar (revocar) la asignación
    repository.delete(targetId);
} 
    private void removePrimaryFromOtherRoles(UserIdentityId userId) {
        repository.findByUserIdAndIsPrimary(userId, true)
                .ifPresent(currentPrimary -> repository.updatePrimary(currentPrimary.getId(), false));
    }
}