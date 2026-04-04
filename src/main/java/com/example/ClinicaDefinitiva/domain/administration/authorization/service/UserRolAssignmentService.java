package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.RolNotFoundException;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.UserRolAssignmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRolAssignmentService {

    private final UserRolAssignmentRepository assignmentRepository;
    private final RolRepository rolRepository;

    public UserRolAssignmentService(UserRolAssignmentRepository assignmentRepository,
                                    RolRepository rolRepository) {
        this.assignmentRepository = assignmentRepository;
        this.rolRepository = rolRepository;
    }

  
    public List<Rol> getActiveRoles(UserIdentityId userIdentityId) {
    Page<UserRolAssignment> allAssignments = assignmentRepository
            .findByUserId(userIdentityId, Pageable.unpaged());
    List<UserRolAssignment> activeAssignments = allAssignments.stream()
            .filter(UserRolAssignment::isCurrentlyActive)
            .collect(Collectors.toList());

    return activeAssignments.stream()
            .map(assignment -> rolRepository.findById(assignment.getRolId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
}

    public Rol getPrimaryRole(UserIdentityId userIdentityId) {
        return assignmentRepository
                .findByUserIdAndIsPrimary(userIdentityId, true)
                .filter(UserRolAssignment::isCurrentlyActive)
                .flatMap(assignment -> rolRepository.findById(assignment.getRolId()))
                .orElseThrow(() -> new IllegalStateException("User has no primary role"));
    }

    public UserRolAssignment assignRole(UserIdentityId userIdentityId, RolId rolId, boolean isPrimary) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RolNotFoundException("" + rolId));

        if (rol.getStatusRol() == RolStatus.INACTIVE) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_INACTIVE_ROLE,
                    EntityContext.ASSIGNMENT
            );
        }

        List<Rol> activeRoles = getActiveRoles(userIdentityId);
        boolean alreadyActive = activeRoles.stream()
                .anyMatch(r -> r.getId().equals(rolId));

        if (alreadyActive) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_DUPLICATE_ACTIVE,
                    EntityContext.ASSIGNMENT
            );
        }

        if (isPrimary) {
            removePrimaryFromOtherRoles(userIdentityId);
        }

        UserRolAssignment assignment = UserRolAssignment.assignPermanent(userIdentityId, rolId, isPrimary);
        return assignmentRepository.save(assignment);
    }

    public UserRolAssignment assignTemporaryRole(UserIdentityId userIdentityId, RolId rolId,
                                                 LocalDate validFrom, LocalDate validTo, boolean isPrimary) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RolNotFoundException(""));

        if (rol.getStatusRol() == RolStatus.INACTIVE) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_INACTIVE_ROLE,
                    EntityContext.ASSIGNMENT
            );
        }

        List<Rol> activeRoles = getActiveRoles(userIdentityId);
        boolean alreadyActive = activeRoles.stream()
                .anyMatch(r -> r.getId().equals(rolId));

        if (alreadyActive) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_DUPLICATE_ACTIVE,
                    EntityContext.ASSIGNMENT
            );
        }

        UserRolAssignment assignment = UserRolAssignment.assignTemporary(userIdentityId, rolId, validFrom, validTo, isPrimary);
        return assignmentRepository.save(assignment);
    }

    public void revokeRole(UserIdentityId userIdentityId, RolId rolId) {
        List<Rol> activeRoles = getActiveRoles(userIdentityId);

        boolean isLastActive = activeRoles.size() == 1 &&
                activeRoles.get(0).getId().equals(rolId);

        if (isLastActive) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_CANNOT_REVOKE_LAST_INDIVIDUAL,
                    EntityContext.ASSIGNMENT
            );
        }

        Page<UserRolAssignment> assignmentsPage = assignmentRepository
                .findByUserIdAndRolId(userIdentityId, rolId, Pageable.unpaged());
        assignmentsPage.forEach(assignment -> {
            assignment.revoke();
            assignmentRepository.save(assignment);
        });
    }

    public void revokeAllRoles(UserIdentityId userIdentityId) {
        Page<UserRolAssignment> assignmentsPage = assignmentRepository
                .findByUserId(userIdentityId, Pageable.unpaged());
        assignmentsPage.forEach(assignment -> {
            assignment.revoke();
            assignmentRepository.save(assignment);
        });
    }

    private void removePrimaryFromOtherRoles(UserIdentityId userIdentityId) {
        assignmentRepository.findByUserIdAndIsPrimary(userIdentityId, true)
                .ifPresent(currentPrimary -> 
                    assignmentRepository.updatePrimary(currentPrimary.getId(), false)
                );
    }
}