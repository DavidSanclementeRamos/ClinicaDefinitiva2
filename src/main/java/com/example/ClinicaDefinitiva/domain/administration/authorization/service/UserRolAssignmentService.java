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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio para gestión de roles de usuario
 */
@Service
public class UserRolAssignmentService {

    private final UserRolAssignmentRepository assignmentRepository;
    private final RolRepository rolRepository;

    public UserRolAssignmentService(UserRolAssignmentRepository assignmentRepository,
                                    RolRepository rolRepository) {
        this.assignmentRepository = assignmentRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Obtiene todos los roles activos de un usuario
     */
    public List<Rol> getActiveRoles(UserIdentityId userIdentityId) {
        List<UserRolAssignment> activeAssignments = assignmentRepository
                .findByUserId(userIdentityId)
                .stream()
                .filter(UserRolAssignment::isCurrentlyActive)
                .toList();

        return activeAssignments.stream()
                .map(assignment -> rolRepository.findById(assignment.getRolId()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el rol principal del usuario (para UI)
     */
    public Rol getPrimaryRole(UserIdentityId userIdentityId) {
        return assignmentRepository
                .findByUserIdAndIsPrimary(userIdentityId, true)
                .filter(UserRolAssignment::isCurrentlyActive)
                .flatMap(assignment -> rolRepository.findById(assignment.getRolId()))
                .orElseThrow(() -> new IllegalStateException("User has no primary role"));
    }

    public UserRolAssignment assignRole(UserIdentityId userIdentityId, RolId rolId, boolean isPrimary) {
        // Validar que el rol esté activo
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RolNotFoundException("" + rolId));

        if (rol.getStatusRol() == RolStatus.INACTIVE) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_INACTIVE_ROLE,
                    EntityContext.ASSIGNMENT
            );
        }

        // Validar que el rol no esté ya activo para el usuario
        List<Rol> activeRoles = getActiveRoles(userIdentityId);
        boolean alreadyActive = activeRoles.stream()
                .anyMatch(r -> r.getId().equals(rolId));

        if (alreadyActive) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_DUPLICATE_ACTIVE,
                    EntityContext.ASSIGNMENT
            );
        }

        // Si es primario, quitar primary de otros roles
        if (isPrimary) {
            removePrimaryFromOtherRoles(userIdentityId);
        }

        UserRolAssignment assignment = UserRolAssignment.assignPermanent(userIdentityId, rolId, isPrimary);
        return assignmentRepository.save(assignment);
    }

    public UserRolAssignment assignTemporaryRole(UserIdentityId userIdentityId, RolId rolId,
                                                 LocalDate validFrom, LocalDate validTo, boolean isPrimary) {
        // Validar que el rol esté activo
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RolNotFoundException(""));

        if (rol.getStatusRol() == RolStatus.INACTIVE) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_INACTIVE_ROLE,
                    EntityContext.ASSIGNMENT
            );
        }

        // Validar que el rol no esté ya activo para el usuario
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



    /**
     * Revoca un rol específico
     */

    public void revokeRole(UserIdentityId userIdentityId, RolId rolId) {
        // Obtener todos los roles activos del usuario
        List<Rol> activeRoles = getActiveRoles(userIdentityId);

        // Validar si el rol a revocar es el único activo
        boolean isLastActive = activeRoles.size() == 1 &&
                activeRoles.get(0).getId().equals(rolId);

        if (isLastActive) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_CANNOT_REVOKE_LAST_INDIVIDUAL,
                    EntityContext.ASSIGNMENT
            );
        }

        // Revocar todas las asignaciones del rol
        List<UserRolAssignment> assignments = assignmentRepository
                .findByUserIdAndRolId(userIdentityId, rolId);

        assignments.forEach(assignment -> {
            assignment.revoke();
            assignmentRepository.save(assignment);
        });
    }


    /**
     * Revoca todos los roles de un usuario
     */
    public void revokeAllRoles(UserIdentityId userIdentityId) {
        List<UserRolAssignment> assignments = assignmentRepository.findByUserId(userIdentityId);
        assignments.forEach(assignment -> {
            assignment.revoke();
            assignmentRepository.save(assignment);
        });
    }


    /**
     * ⚠️ removePrimaryFromOtherRoles hace update uno por uno → puede ser costoso.
     *
     * Mejor usar un método batch en el repositorio (updatePrimaryByUserId(userId, false)).
     */
    private void removePrimaryFromOtherRoles(UserIdentityId userIdentityId) {
        List<UserRolAssignment> primaryAssignments = assignmentRepository
                .findByUserIdAndIsPrimary(userIdentityId, true)
                .stream()
                .toList();

        // Esto debería usar un método en el repositorio o actualizar en batch
        // Por simplicidad, se muestra el concepto
        primaryAssignments.forEach(assignment -> {
            // Marcar como no primario (requiere método en UserRolAssignment)
            assignmentRepository.updatePrimary(assignment.getId(), false);
        });
    }
}

