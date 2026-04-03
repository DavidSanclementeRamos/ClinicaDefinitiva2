package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolAssignmentServiceTest {

    @Mock
    private UserRolAssignmentRepository assignmentRepository;
    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UserRolAssignmentService service;

    private static final UserIdentityId USER_ID = UserIdentityId.from(1L);
    private static final RolId ROL_ID = RolId.of(1L);

    @Test
    @DisplayName("Asignar rol activo exitosamente")
    void assignRole_success() {
        Rol rol = mock(Rol.class);
        when(rol.getStatusRol()).thenReturn(RolStatus.ACTIVE);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));
        
        // ✅ Page vacía
        Page<UserRolAssignment> emptyPage = new PageImpl<>(List.of());
        when(assignmentRepository.findByUserId(eq(USER_ID), any(Pageable.class))).thenReturn(emptyPage);
        
        when(assignmentRepository.save(any(UserRolAssignment.class))).thenAnswer(i -> i.getArgument(0));

        UserRolAssignment result = service.assignRole(USER_ID, ROL_ID, true);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getRolId()).isEqualTo(ROL_ID);
        assertThat(result.isPrimary()).isTrue();
        verify(assignmentRepository).save(any(UserRolAssignment.class));
    }

    @Test
    @DisplayName("Asignar rol inactivo lanza excepción")
    void assignRole_inactive_throws() {
        Rol rol = mock(Rol.class);
        when(rol.getStatusRol()).thenReturn(RolStatus.INACTIVE);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));

        assertThatThrownBy(() -> service.assignRole(USER_ID, ROL_ID, true))
                .isInstanceOf(BusinessRuleViolationException.class);
        
        verify(assignmentRepository, never()).findByUserId(any(), any());
    }

    @Test
    @DisplayName("Asignar rol ya activo lanza excepción")
    void assignRole_alreadyActive_throws() {
        Rol rol = mock(Rol.class);
        when(rol.getStatusRol()).thenReturn(RolStatus.ACTIVE);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));
        
        // Simular asignación existente
        UserRolAssignment existingAssignment = mock(UserRolAssignment.class);
        when(existingAssignment.isCurrentlyActive()).thenReturn(true);
        when(existingAssignment.getRolId()).thenReturn(ROL_ID);
        
        Page<UserRolAssignment> pageWithAssignment = new PageImpl<>(List.of(existingAssignment));
        when(assignmentRepository.findByUserId(eq(USER_ID), any(Pageable.class))).thenReturn(pageWithAssignment);
        
        // getActiveRoles necesita obtener el rol existente
        Rol existingRol = mock(Rol.class);
        when(existingRol.getId()).thenReturn(ROL_ID);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(existingRol));
        
        assertThatThrownBy(() -> service.assignRole(USER_ID, ROL_ID, true))
                .isInstanceOf(BusinessRuleViolationException.class);
        
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Revocar último rol activo lanza excepción")
    void revokeLastRole_throws() {
        // Configurar rol
        Rol rol = mock(Rol.class);
        when(rol.getId()).thenReturn(ROL_ID);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));
        
        // Configurar asignación única
        UserRolAssignment assignment = mock(UserRolAssignment.class);
        when(assignment.isCurrentlyActive()).thenReturn(true);
        when(assignment.getRolId()).thenReturn(ROL_ID);
        
        Page<UserRolAssignment> singlePage = new PageImpl<>(List.of(assignment));
        when(assignmentRepository.findByUserId(eq(USER_ID), any(Pageable.class))).thenReturn(singlePage);
        
        Page<UserRolAssignment> assignmentsForRevoke = new PageImpl<>(List.of(assignment));
        when(assignmentRepository.findByUserIdAndRolId(eq(USER_ID), eq(ROL_ID), any(Pageable.class)))
                .thenReturn(assignmentsForRevoke);
        
        assertThatThrownBy(() -> service.revokeRole(USER_ID, ROL_ID))
                .isInstanceOf(BusinessRuleViolationException.class);
        
        verify(assignment, never()).revoke();
    }

    @Test
    @DisplayName("Revocar rol cuando hay múltiples roles activos")
    void revokeRole_success() {
        RolId otherRolId = RolId.of(2L);
        
        // Configurar roles
        Rol rol1 = mock(Rol.class);
        when(rol1.getId()).thenReturn(ROL_ID);
        Rol rol2 = mock(Rol.class);
        when(rol2.getId()).thenReturn(otherRolId);
        
        // Configurar asignaciones
        UserRolAssignment assignment1 = mock(UserRolAssignment.class);
        when(assignment1.isCurrentlyActive()).thenReturn(true);
        when(assignment1.getRolId()).thenReturn(ROL_ID);
        
        UserRolAssignment assignment2 = mock(UserRolAssignment.class);
        when(assignment2.isCurrentlyActive()).thenReturn(true);
        when(assignment2.getRolId()).thenReturn(otherRolId);
        
        Page<UserRolAssignment> pageWithTwo = new PageImpl<>(List.of(assignment1, assignment2));
        when(assignmentRepository.findByUserId(eq(USER_ID), any(Pageable.class))).thenReturn(pageWithTwo);
        
        Page<UserRolAssignment> revokePage = new PageImpl<>(List.of(assignment1));
        when(assignmentRepository.findByUserIdAndRolId(eq(USER_ID), eq(ROL_ID), any(Pageable.class)))
                .thenReturn(revokePage);
        
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol1));
        when(rolRepository.findById(otherRolId)).thenReturn(Optional.of(rol2));
        
        assertThatCode(() -> service.revokeRole(USER_ID, ROL_ID))
                .doesNotThrowAnyException();
        
        verify(assignment1).revoke();
        verify(assignmentRepository).save(assignment1);
        verify(assignment2, never()).revoke();
    }
}