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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
        // Stubs necesarios para el flujo exitoso
        Rol rol = mock(Rol.class);
        when(rol.getStatusRol()).thenReturn(RolStatus.ACTIVE);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));
        when(assignmentRepository.findByUserId(USER_ID)).thenReturn(List.of());
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
        // Solo stubs necesarios para la excepción
        Rol rol = mock(Rol.class);
        when(rol.getStatusRol()).thenReturn(RolStatus.INACTIVE);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));

        assertThatThrownBy(() -> service.assignRole(USER_ID, ROL_ID, true))
                .isInstanceOf(BusinessRuleViolationException.class);
        
        verify(assignmentRepository, never()).findByUserId(any());
    }

    @Test
    @DisplayName("Asignar rol ya activo lanza excepción")
    void assignRole_alreadyActive_throws() {
        // Stubs necesarios para detectar rol ya activo
        Rol rol = mock(Rol.class);
        when(rol.getStatusRol()).thenReturn(RolStatus.ACTIVE);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));
        
        // Configurar asignación existente
        UserRolAssignment existingAssignment = mock(UserRolAssignment.class);
        when(existingAssignment.isCurrentlyActive()).thenReturn(true);
        when(existingAssignment.getRolId()).thenReturn(ROL_ID);
        
        // Para getActiveRoles - necesitamos que rolRepository.findById devuelva el rol existente
        // pero NO necesitamos configurar existingRol.getId() porque la comparación
        // se hace con rolId (ROL_ID) directamente, no con rol.getId()
        when(assignmentRepository.findByUserId(USER_ID)).thenReturn(List.of(existingAssignment));
        
        // getActiveRoles llama a rolRepository.findById para cada asignación
        Rol existingRol = mock(Rol.class);
        // NO configurar existingRol.getId() - no es necesario porque en assignRole,
        // la comparación se hace con rolId directamente: r.getId().equals(rolId)
        // Pero si no se configura, getActiveRoles lo obtiene y luego se compara en assignRole
        // Para evitar NPE, sí necesitamos configurarlo
        when(existingRol.getId()).thenReturn(ROL_ID);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(existingRol));
        
        assertThatThrownBy(() -> service.assignRole(USER_ID, ROL_ID, true))
                .isInstanceOf(BusinessRuleViolationException.class);
        
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Revocar último rol activo lanza excepción")
    void revokeLastRole_throws() {
        // Configurar rol para la validación
        Rol rol = mock(Rol.class);
        when(rol.getId()).thenReturn(ROL_ID);
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol));
        
        // Configurar asignación única
        UserRolAssignment assignment = mock(UserRolAssignment.class);
        when(assignment.isCurrentlyActive()).thenReturn(true);
        when(assignment.getRolId()).thenReturn(ROL_ID);
        
        // Para getActiveRoles
        when(assignmentRepository.findByUserId(USER_ID)).thenReturn(List.of(assignment));
        // getActiveRoles llama a rolRepository.findById para cada asignación
        // Ya configurado arriba: when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol))
        
        // Para findByUserIdAndRolId
        when(assignmentRepository.findByUserIdAndRolId(USER_ID, ROL_ID)).thenReturn(List.of(assignment));
        
        // NO configurar assignmentRepository.save - no se llega a usar

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
        
        when(assignmentRepository.findByUserId(USER_ID)).thenReturn(List.of(assignment1, assignment2));
        when(assignmentRepository.findByUserIdAndRolId(USER_ID, ROL_ID)).thenReturn(List.of(assignment1));
        
        // Para getActiveRoles
        when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(rol1));
        when(rolRepository.findById(otherRolId)).thenReturn(Optional.of(rol2));
        
        assertThatCode(() -> service.revokeRole(USER_ID, ROL_ID))
                .doesNotThrowAnyException();
        
        verify(assignment1).revoke();
        verify(assignmentRepository).save(assignment1);
        verify(assignment2, never()).revoke();
    }
}