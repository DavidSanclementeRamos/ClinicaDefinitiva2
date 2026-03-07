
package com.example.ClinicaDefinitiva.domain.adminitration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class UserRolAssignmentServiceTest {

    @Test
    void shouldReturnActiveRoles() {
        UserRolAssignmentRepository assignmentRepo = mock(UserRolAssignmentRepository.class);
        RolRepository rolRepo = mock(RolRepository.class);

        UserIdentityId userId = UserIdentityId.from(1L);
        RolId rolId = RolId.of(10L);
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Admin", false, true, true, RolStatus.ACTIVE);

        UserRolAssignment assignment = UserRolAssignment.assignPermanent(userId, rolId, true);
        when(assignmentRepo.findByUserId(userId)).thenReturn(List.of(assignment));
        when(rolRepo.findById(rolId)).thenReturn(Optional.of(rol));

        UserRolAssignmentService service = new UserRolAssignmentService(assignmentRepo, rolRepo);

        List<Rol> activeRoles = service.getActiveRoles(userId);
        assertEquals(1, activeRoles.size());
        assertEquals("Admin", activeRoles.get(0).getDescription());
    }

    @Test
    void shouldReturnPrimaryRole() {
        UserRolAssignmentRepository assignmentRepo = mock(UserRolAssignmentRepository.class);
        RolRepository rolRepo = mock(RolRepository.class);

        UserIdentityId userId = UserIdentityId.from(2L);
        RolId rolId = RolId.of(20L);
        Rol rol = new Rol(RolEnum.RECEPTIONIST, "Receptionist", false, true, true, RolStatus.ACTIVE);

        UserRolAssignment assignment = UserRolAssignment.assignPermanent(userId, rolId, true);
        when(assignmentRepo.findByUserIdAndIsPrimary(userId, true)).thenReturn(Optional.of(assignment));
        when(rolRepo.findById(rolId)).thenReturn(Optional.of(rol));

        UserRolAssignmentService service = new UserRolAssignmentService(assignmentRepo, rolRepo);

        Rol primaryRole = service.getPrimaryRole(userId);
        assertEquals("Receptionist", primaryRole.getDescription());
    }

    @Test
    void shouldAssignPermanentRoleSuccessfully() {
        UserRolAssignmentRepository assignmentRepo = mock(UserRolAssignmentRepository.class);
        RolRepository rolRepo = mock(RolRepository.class);

        UserIdentityId userId = UserIdentityId.from(3L);
        RolId rolId = RolId.of(30L);
        Rol rol = new Rol(RolEnum.DENTIST, "Dentist", false, true, true, RolStatus.ACTIVE);

        when(rolRepo.findById(rolId)).thenReturn(Optional.of(rol));
        when(assignmentRepo.findByUserId(userId)).thenReturn(List.of());
        when(assignmentRepo.save(any(UserRolAssignment.class))).thenAnswer(inv -> inv.getArgument(0));

        UserRolAssignmentService service = new UserRolAssignmentService(assignmentRepo, rolRepo);

        UserRolAssignment assignment = service.assignRole(userId, rolId, true);
        assertTrue(assignment.isPrimary());
        assertEquals(rolId, assignment.getRolId());
    }

    @Test
    void shouldThrowExceptionWhenAssigningInactiveRole() {
        UserRolAssignmentRepository assignmentRepo = mock(UserRolAssignmentRepository.class);
        RolRepository rolRepo = mock(RolRepository.class);

        UserIdentityId userId = UserIdentityId.from(4L);
        RolId rolId = RolId.of(40L);
        Rol rol = new Rol(RolEnum.PATIENT, "Inactive Role", false, true, true, RolStatus.INACTIVE);

        when(rolRepo.findById(rolId)).thenReturn(Optional.of(rol));

        UserRolAssignmentService service = new UserRolAssignmentService(assignmentRepo, rolRepo);

        assertThrows(BusinessRuleViolationException.class, () ->
                service.assignRole(userId, rolId, false));
    }

    @Test
    void shouldRevokeRoleSuccessfully() {
        UserRolAssignmentRepository assignmentRepo = mock(UserRolAssignmentRepository.class);
        RolRepository rolRepo = mock(RolRepository.class);

        UserIdentityId userId = UserIdentityId.from(5L);
        RolId rolId = RolId.of(50L);
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Admin", false, true, true, RolStatus.ACTIVE);

        UserRolAssignment assignment = UserRolAssignment.assignPermanent(userId, rolId, false);

        when(assignmentRepo.findByUserId(userId)).thenReturn(List.of(assignment, assignment));
        when(assignmentRepo.findByUserIdAndRolId(userId, rolId)).thenReturn(List.of(assignment));

        UserRolAssignmentService service = new UserRolAssignmentService(assignmentRepo, rolRepo);

        assertDoesNotThrow(() -> service.revokeRole(userId, rolId));
    }
}

