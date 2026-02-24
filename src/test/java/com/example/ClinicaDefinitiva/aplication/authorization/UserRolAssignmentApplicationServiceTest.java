package com.example.ClinicaDefinitiva.aplication.authorization;


import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.permission.UserRolAssignmentNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment.AssignmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.userRolAssignment.AssignmentWriteMapper;
import com.example.ClinicaDefinitiva.application.service.adminitration.authorization.UserRolAssignmentApplicationService;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.Sector;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test Suite Completa para UserRolAssignmentApplicationService
 *
 * Cobertura:
 * - Asignaciones permanentes (savePermanent)
 * - Asignaciones temporales (saveTemporary)
 * - Verificación de estado activo (isActiveAt, isCurrentlyActive)
 * - Extensión de vigencia (extend)
 * - Revocación (revoke, revokeRole)
 * - Consultas (findById, findByUserId, findByUserIdAndRolId, findByUserIdAndIsPrimary)
 * - Validaciones de autorización
 * - Manejo de errores
 * - Casos edge
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserRolAssignmentApplicationService - Tests Completos")
class UserRolAssignmentApplicationServiceTest {

    @Mock
    private UserRolAssignmentService userRolService;

    @Mock
    private AssignmentWriteMapper writeMapper;

    @Mock
    private AssignmentReadMapper readMapper;

    @Mock
    private UserRolAssignmentRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private ReceptionRepository receptionRepository;

    @InjectMocks
    private UserRolAssignmentApplicationService service;

    // Test Data
    private UserIdentityId requesterId;
    private RolId requesterRolId;
    private UserIdentityId targetUserIdentityId;
    private RolId targetRolId;
    private UserRolAssignmentId assignmentId;
    private Receptionist receptionist;
    private UserRolAssignment assignment;
    private ReadAssignmentDto readAssignmentDto;
    private CreateAssignmentPermanentDto createPermanentDto;
    private CreateAssignmentTemporaryDto createTemporaryDto;

    @BeforeEach
    void setUp() {
        requesterId = UserIdentityId.from(1L);
        requesterRolId = RolId.of(1L);
        targetUserIdentityId = UserIdentityId.from(2L);
        targetRolId = RolId.of(2L);
        assignmentId = UserRolAssignmentId.of(1L);

        // Setup Receptionist
        receptionist = mock(Receptionist.class);
        Sector sector = mock(Sector.class);
        when(sector.getDescription()).thenReturn("RECURSOS_HUMANOS");
        when(receptionist.getSector()).thenReturn(sector);

        // Setup Assignment
        assignment = UserRolAssignment.assignPermanent(targetUserIdentityId, targetRolId, true);
        assignment.setId(assignmentId);

        // Setup DTOs
       // readAssignmentDto = new ReadAssignmentDto();
        //createPermanentDto = new CreateAssignmentPermanentDto();
      //  createTemporaryDto = new CreateAssignmentTemporaryDto();
    }

    // ===================================================================================
    // NESTED TEST CLASS: Permanent Assignment Operations
    // ===================================================================================
    @Nested
    @DisplayName("Permanent Assignment Operations")
    class PermanentAssignmentOperations {

        @Test
        @DisplayName("savePermanent - Debe crear asignación permanente cuando está autorizado")
        void savePermanent_WhenAuthorized_ShouldCreatePermanentAssignment() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreatePermanent(createPermanentDto)).thenReturn(assignment);
            when(userRolService.assignRole(targetUserIdentityId, targetRolId, true)).thenReturn(assignment);
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When
            ReadAssignmentDto result = service.savePermanent(createPermanentDto, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(readAssignmentDto);

            verify(receptionRepository).findByUserId(requesterId);
            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(userRolService).assignRole(targetUserIdentityId, targetRolId, true);
            verify(readMapper).toReadDto(assignment);
        }

        @Test
        @DisplayName("savePermanent - Debe lanzar excepción cuando receptionist no existe")
        void savePermanent_WhenReceptionistNotFound_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.savePermanent(createPermanentDto, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            verify(receptionRepository).findByUserId(requesterId);
            verify(userRolService, never()).assignRole(any(), any(), anyBoolean());
        }

        @Test
        @DisplayName("savePermanent - Debe lanzar excepción cuando no está autorizado")
        void savePermanent_WhenNotAuthorized_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> service.savePermanent(createPermanentDto, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_UNAUTHORIZED_CREATION);

            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(userRolService, never()).assignRole(any(), any(), anyBoolean());
        }

        @Test
        @DisplayName("savePermanent - Debe construir SecurityContext correctamente")
        void savePermanent_ShouldBuildCorrectSecurityContext() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreatePermanent(createPermanentDto)).thenReturn(assignment);
            when(userRolService.assignRole(any(), any(), anyBoolean())).thenReturn(assignment);
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When
            service.savePermanent(createPermanentDto, requesterId, requesterRolId);

            // Then
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            verify(authorizationService).isAuthorized(eq(requesterRolId), contextCaptor.capture());

            SecurityContext context = contextCaptor.getValue();
            assertThat(context.getPermission().getResource())
                    .isEqualTo(ResourceCatalog.of(ResourceCatalog.BasicResource.ASSIGNMENT));
            assertThat(context.getPermission().getAction())
                    .isEqualTo(ActionCatalog.of(ActionCatalog.BasicAction.CREATE));
            assertThat(context.getRequestingUserId()).isEqualTo(requesterId);
            assertThat(context.getAttribute("sector", String.class)).hasValue("RECURSOS_HUMANOS");
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Temporary Assignment Operations
    // ===================================================================================
    @Nested
    @DisplayName("Temporary Assignment Operations")
    class TemporaryAssignmentOperations {

        private LocalDate validFrom;
        private LocalDate validTo;

        @BeforeEach
        void setUp() {
            validFrom = LocalDate.now();
            validTo = LocalDate.now().plusDays(30);
        }

        @Test
        @DisplayName("saveTemporary - Debe crear asignación temporal cuando está autorizado")
        void saveTemporary_WhenAuthorized_ShouldCreateTemporaryAssignment() {
            // Given
            UserRolAssignment tempAssignment = UserRolAssignment.assignTemporary(
                    targetUserIdentityId, targetRolId, validFrom, validTo, false);
            tempAssignment.setId(assignmentId);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreateTemporary(createTemporaryDto)).thenReturn(tempAssignment);
            when(userRolService.assignTemporaryRole(targetUserIdentityId, targetRolId, validFrom, validTo,false)).thenReturn(tempAssignment);
            when(readMapper.toReadDto(tempAssignment)).thenReturn(readAssignmentDto);

            // When
            ReadAssignmentDto result = service.saveTemporary(createTemporaryDto, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(readAssignmentDto);

            verify(receptionRepository).findByUserId(requesterId);
            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(userRolService).assignTemporaryRole(targetUserIdentityId, targetRolId, validFrom, validTo,false);
        }

        @Test
        @DisplayName("saveTemporary - Debe lanzar excepción cuando receptionist no existe")
        void saveTemporary_WhenReceptionistNotFound_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.saveTemporary(createTemporaryDto, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            verify(userRolService, never()).assignTemporaryRole(any(), any(), any(), any(),any());
        }

        @Test
        @DisplayName("saveTemporary - Debe lanzar excepción cuando no está autorizado")
        void saveTemporary_WhenNotAuthorized_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> service.saveTemporary(createTemporaryDto, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_UNAUTHORIZED_CREATION);

            verify(userRolService, never()).assignTemporaryRole(any(), any(), any(), any(),any());
        }

        @Test
        @DisplayName("saveTemporary - Debe construir SecurityContext con CREATE_TEMPORARY action")
        void saveTemporary_ShouldBuildSecurityContextWithCreateTemporaryAction() {
            // Given
            UserRolAssignment tempAssignment = UserRolAssignment.assignTemporary(
                    targetUserIdentityId, targetRolId, validFrom, validTo, false);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreateTemporary(createTemporaryDto)).thenReturn(tempAssignment);
            when(userRolService.assignTemporaryRole(any(), any(), any(), any(),any())).thenReturn(tempAssignment);
            when(readMapper.toReadDto(tempAssignment)).thenReturn(readAssignmentDto);

            // When
            service.saveTemporary(createTemporaryDto, requesterId, requesterRolId);

            // Then
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            verify(authorizationService).isAuthorized(eq(requesterRolId), contextCaptor.capture());

            SecurityContext context = contextCaptor.getValue();
            assertThat(context.getPermission().getAction())
                    .isEqualTo(ActionCatalog.of(ActionCatalog.BasicAction.CREATE_TEMPORARY));
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Active Status Check Operations
    // ===================================================================================
    @Nested
    @DisplayName("Active Status Check Operations")
    class ActiveStatusCheckOperations {

        @Test
        @DisplayName("isActiveAt - Debe verificar si está activo en fecha específica")
        void isActiveAt_WithSpecificDate_ShouldCheckActiveStatus() {
            // Given
            LocalDate checkDate = LocalDate.now();

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(assignment.isActiveAt(checkDate)).thenReturn(true);

            // When
            boolean result = service.isActiveAt(assignmentId, checkDate, requesterId, requesterRolId);

            // Then
            assertThat(result).isTrue();

            verify(repository).findById(assignmentId);
            verify(assignment).isActiveAt(checkDate);
        }

        @Test
        @DisplayName("isActiveAt - Debe lanzar excepción cuando assignment no existe")
        void isActiveAt_WhenAssignmentNotFound_ShouldThrowException() {
            // Given
            LocalDate checkDate = LocalDate.now();

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.isActiveAt(assignmentId, checkDate, requesterId, requesterRolId))
                    .isInstanceOf(UserRolAssignmentNotFoundException.class);

            verify(repository).findById(assignmentId);
        }

        @Test
        @DisplayName("isActiveAt - Debe lanzar excepción cuando no está autorizado")
        void isActiveAt_WhenNotAuthorized_ShouldThrowException() {
            // Given
            LocalDate checkDate = LocalDate.now();

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> service.isActiveAt(assignmentId, checkDate, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_PERMISSION_DENIED);

            verify(repository, never()).findById(any());
        }

        @Test
        @DisplayName("isCurrentlyActive - Debe verificar si está activo actualmente")
        void isCurrentlyActive_ShouldCheckCurrentActiveStatus() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(assignment.isCurrentlyActive()).thenReturn(true);

            // When
            boolean result = service.isCurrentlyActive(assignmentId, requesterId, requesterRolId);

            // Then
            assertThat(result).isTrue();

            verify(repository).findById(assignmentId);
            verify(assignment).isCurrentlyActive();
        }

        @Test
        @DisplayName("isCurrentlyActive - Debe retornar false cuando no está activo")
        void isCurrentlyActive_WhenNotActive_ShouldReturnFalse() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(assignment.isCurrentlyActive()).thenReturn(false);

            // When
            boolean result = service.isCurrentlyActive(assignmentId, requesterId, requesterRolId);

            // Then
            assertThat(result).isFalse();
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Extension Operations
    // ===================================================================================
    @Nested
    @DisplayName("Extension Operations")
    class ExtensionOperations {

        @Test
        @DisplayName("extend - Debe extender vigencia cuando está autorizado")
        void extend_WhenAuthorized_ShouldExtendValidity() {
            // Given
            LocalDate newEndDate = LocalDate.now().plusDays(60);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(repository.save(assignment)).thenReturn(assignment);

            // When
            service.extend(assignmentId, newEndDate, requesterId, requesterRolId);

            // Then
            verify(repository).findById(assignmentId);
            verify(assignment).extend(newEndDate);
            verify(repository).save(assignment);
        }

        @Test
        @DisplayName("extend - Debe lanzar excepción cuando assignment no existe")
        void extend_WhenAssignmentNotFound_ShouldThrowException() {
            // Given
            LocalDate newEndDate = LocalDate.now().plusDays(60);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.extend(assignmentId, newEndDate, requesterId, requesterRolId))
                    .isInstanceOf(UserRolAssignmentNotFoundException.class);

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("extend - Debe lanzar excepción cuando no está autorizado")
        void extend_WhenNotAuthorized_ShouldThrowException() {
            // Given
            LocalDate newEndDate = LocalDate.now().plusDays(60);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> service.extend(assignmentId, newEndDate, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_PERMISSION_DENIED);

            verify(repository, never()).findById(any());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Revoke Operations
    // ===================================================================================
    @Nested
    @DisplayName("Revoke Operations")
    class RevokeOperations {

        @Test
        @DisplayName("revoke - Debe revocar asignación cuando está autorizado")
        void revoke_WhenAuthorized_ShouldRevokeAssignment() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(repository.save(assignment)).thenReturn(assignment);

            // When
            service.revokeRol( requesterId, requesterRolId,null,null);

            // Then
            verify(repository).findById(assignmentId);
            verify(assignment).revoke();
            verify(repository).save(assignment);
        }

        @Test
        @DisplayName("revoke - Debe lanzar excepción cuando assignment no existe")
        void revoke_WhenAssignmentNotFound_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.empty());

            // When & Then
           // assertThatThrownBy(() -> service.revokeRol(assignmentId, requesterId, requesterRolId))
               //     .isInstanceOf(UserRolAssignmentNotFoundException.class);

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("revokeRole - Debe revocar rol específico cuando está autorizado")
        void revokeRole_WhenAuthorized_ShouldRevokeSpecificRole() {
            // Given
            List<UserRolAssignment> assignments = Arrays.asList(assignment);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findByUserIdAndRolId(targetUserIdentityId, targetRolId)).thenReturn(assignments);
            doNothing().when(userRolService).revokeRole(targetUserIdentityId, targetRolId);

            // When
           // service.revokeAllRol(targetUserIdentityId, targetRolId, requesterId, requesterRolId);

            // Then
            verify(repository).findByUserIdAndRolId(targetUserIdentityId, targetRolId);
            verify(userRolService).revokeRole(targetUserIdentityId, targetRolId);
        }

        @Test
        @DisplayName("revokeRole - Debe lanzar excepción cuando no hay asignaciones")
        void revokeRole_WhenNoAssignments_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findByUserIdAndRolId(targetUserIdentityId, targetRolId)).thenReturn(Collections.emptyList());

            // When & Then
          //  assertThatThrownBy(() -> service.revokeAllRol(targetUserIdentityId, targetRolId, requesterId, requesterRolId))
                   // .isInstanceOf(UserRolAssignmentNotFoundException.class);

            verify(userRolService, never()).revokeRole(any(), any());
        }

        @Test
        @DisplayName("revokeRole - Debe lanzar excepción cuando no está autorizado")
        void revokeRole_WhenNotAuthorized_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            //assertThatThrownBy(() -> service.revokeAllRol(targetUserIdentityId, targetRolId, requesterId, requesterRolId))
                   // .isInstanceOf(BusinessRuleViolationException.class)
                   // .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_ASSIGNMENT_UNAUTHORIZED_REVOKE);

            verify(repository, never()).findByUserIdAndRolId(any(), any());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Query Operations
    // ===================================================================================
    @Nested
    @DisplayName("Query Operations")
    class QueryOperations {

        @Test
        @DisplayName("findById - Debe retornar assignment cuando existe")
        void findById_WhenAssignmentExists_ShouldReturnAssignment() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When
            Optional<ReadAssignmentDto> result = service.findById(assignmentId, requesterId, requesterRolId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(readAssignmentDto);

            verify(repository).findById(assignmentId);
            verify(readMapper).toReadDto(assignment);
        }

        @Test
        @DisplayName("findById - Debe retornar vacío cuando no existe")
        void findById_WhenAssignmentNotFound_ShouldReturnEmpty() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.empty());

            // When
            Optional<ReadAssignmentDto> result = service.findById(assignmentId, requesterId, requesterRolId);

            // Then
            assertThat(result).isEmpty();

            verify(repository).findById(assignmentId);
            verify(readMapper, never()).toReadDto(any());
        }

        @Test
        @DisplayName("findByUserId - Debe retornar todas las asignaciones del usuario")
        void findByUserId_ShouldReturnAllUserAssignments() {
            // Given
            List<UserRolAssignment> assignments = Arrays.asList(assignment, assignment);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findByUserId(targetUserIdentityId)).thenReturn(assignments);
            when(readMapper.toReadDto(any(UserRolAssignment.class))).thenReturn(readAssignmentDto);

            // When
            List<ReadAssignmentDto> result = service.findByUserId(targetUserIdentityId, requesterId, requesterRolId);

            // Then
            assertThat(result).hasSize(2);

            verify(repository).findByUserId(targetUserIdentityId);
            verify(readMapper, times(2)).toReadDto(any(UserRolAssignment.class));
        }

        @Test
        @DisplayName("findByUserId - Debe retornar lista vacía cuando no hay asignaciones")
        void findByUserId_WhenNoAssignments_ShouldReturnEmptyList() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findByUserId(targetUserIdentityId)).thenReturn(Collections.emptyList());

            // When
            List<ReadAssignmentDto> result = service.findByUserId(targetUserIdentityId, requesterId, requesterRolId);

            // Then
            assertThat(result).isEmpty();

            verify(repository).findByUserId(targetUserIdentityId);
            verify(readMapper, never()).toReadDto(any());
        }

        @Test
        @DisplayName("findByUserIdAndIsPrimary - Debe retornar assignment primario")
        void findByUserIdAndIsPrimary_ShouldReturnPrimaryAssignment() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(repository.findByUserIdAndIsPrimary(targetUserIdentityId, true)).thenReturn(Optional.of(assignment));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When
            Optional<ReadAssignmentDto> result = service.findByUserIdAndIsPrimary(
                    targetUserIdentityId, true, requesterId, requesterRolId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(readAssignmentDto);

            verify(repository, times(2)).findByUserIdAndIsPrimary(targetUserIdentityId, true);
            verify(readMapper).toReadDto(assignment);
        }

        @Test
        @DisplayName("findByUserIdAndIsPrimary - Debe retornar vacío cuando no existe primario")
        void findByUserIdAndIsPrimary_WhenNoPrimary_ShouldReturnEmpty() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(repository.findByUserIdAndIsPrimary(targetUserIdentityId, true)).thenReturn(Optional.empty());
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);

            // When
            Optional<ReadAssignmentDto> result = service.findByUserIdAndIsPrimary(
                    targetUserIdentityId, true, requesterId, requesterRolId);

            // Then
            assertThat(result).isEmpty();

            verify(readMapper, never()).toReadDto(any());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Authorization Context Validation
    // ===================================================================================
    @Nested
    @DisplayName("Authorization Context Validation")
    class AuthorizationContextValidation {

        @Test
        @DisplayName("Debe validar sector en todas las operaciones")
        void shouldValidateSectorInAllOperations() {
            // Given - sector inválido
            Receptionist invalidReceptionist = mock(Receptionist.class);
            Sector invalidSector = mock(Sector.class);
            when(invalidSector.getDescription()).thenReturn("");
            when(invalidReceptionist.getSector()).thenReturn(invalidSector);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(invalidReceptionist));

            // La validación de sector vacío depende de la implementación de SecurityContext
            // Este test verifica que el sector es consultado
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            when(authorizationService.isAuthorized(eq(requesterRolId), contextCaptor.capture())).thenReturn(true);
            when(writeMapper.fromCreatePermanent(any())).thenReturn(assignment);
            when(userRolService.assignRole(any(), any(), anyBoolean())).thenReturn(assignment);
            when(readMapper.toReadDto(any())).thenReturn(readAssignmentDto);

            // When
            service.savePermanent(createPermanentDto, requesterId, requesterRolId);

            // Then
            verify(invalidReceptionist).getSector();
        }

        @Test
        @DisplayName("Debe incluir resourceId en SecurityContext donde corresponda")
        void shouldIncludeResourceIdInSecurityContext() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When
            service.findById(assignmentId, requesterId, requesterRolId);

            // Then
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            verify(authorizationService).isAuthorized(eq(requesterRolId), contextCaptor.capture());

            SecurityContext context = contextCaptor.getValue();
            assertThat(context.getAttribute("resourceId", Long.class)).hasValue(assignmentId.getValue());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Edge Cases
    // ===================================================================================
    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("Debe manejar múltiples asignaciones del mismo usuario")
        void shouldHandleMultipleAssignmentsForSameUser() {
            // Given
            UserRolAssignment assignment1 = UserRolAssignment.assignPermanent(targetUserIdentityId, targetRolId, true);
            UserRolAssignment assignment2 = UserRolAssignment.assignPermanent(targetUserIdentityId, RolId.of(3L), false);
            List<UserRolAssignment> assignments = Arrays.asList(assignment1, assignment2);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findByUserId(targetUserIdentityId)).thenReturn(assignments);
            when(readMapper.toReadDto(any(UserRolAssignment.class))).thenReturn(readAssignmentDto);

            // When
            List<ReadAssignmentDto> result = service.findByUserId(targetUserIdentityId, requesterId, requesterRolId);

            // Then
            assertThat(result).hasSize(2);
            verify(readMapper, times(2)).toReadDto(any(UserRolAssignment.class));
        }

        @Test
        @DisplayName("Debe manejar asignaciones temporales expiradas")
        void shouldHandleExpiredTemporaryAssignments() {
            // Given
            LocalDate pastDate = LocalDate.now().minusDays(30);
            UserRolAssignment expiredAssignment = UserRolAssignment.assignTemporary(
                    targetUserIdentityId, targetRolId, pastDate.minusDays(60), pastDate, false);
            expiredAssignment.setId(assignmentId);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(expiredAssignment));
            when(expiredAssignment.isCurrentlyActive()).thenReturn(false);

            // When
            boolean result = service.isCurrentlyActive(assignmentId, requesterId, requesterRolId);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Debe manejar concurrencia en operaciones")
        void shouldHandleConcurrentOperations() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When - simular múltiples llamadas concurrentes
            Optional<ReadAssignmentDto> result1 = service.findById(assignmentId, requesterId, requesterRolId);
            Optional<ReadAssignmentDto> result2 = service.findById(assignmentId, requesterId, requesterRolId);
            Optional<ReadAssignmentDto> result3 = service.findById(assignmentId, requesterId, requesterRolId);

            // Then
            assertThat(result1).isPresent();
            assertThat(result2).isPresent();
            assertThat(result3).isPresent();

            verify(repository, times(3)).findById(assignmentId);
        }

        @Test
        @DisplayName("Debe manejar cambios de estado en cascade")
        void shouldHandleCascadeStateChanges() {
            // Given
            LocalDate newEndDate = LocalDate.now().plusDays(90);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
            when(repository.save(assignment)).thenReturn(assignment);
            doNothing().when(assignment).extend(newEndDate);

            // When
            service.extend(assignmentId, newEndDate, requesterId, requesterRolId);

            // Then
            verify(assignment).extend(newEndDate);
            verify(repository).save(assignment);
        }

        @Test
        @DisplayName("Debe manejar validaciones de fechas en extensiones")
        void shouldHandleDateValidationsInExtension() {
            // Given
            LocalDate invalidDate = LocalDate.now().minusDays(1); // Fecha en el pasado

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));

            // La validación de fecha ocurre en el agregado assignment.extend()
            // Este test verifica que la llamada al método se realiza

            // When & Then
            service.extend(assignmentId, invalidDate, requesterId, requesterRolId);

            verify(assignment).extend(invalidDate);
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Performance and Integration Tests
    // ===================================================================================
    @Nested
    @DisplayName("Performance and Integration Scenarios")
    class PerformanceAndIntegrationScenarios {

        @Test
        @DisplayName("Debe manejar búsquedas con grandes volúmenes de datos")
        void shouldHandleLargeDatasetQueries() {
            // Given
            List<UserRolAssignment> largeList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                UserRolAssignment ass = UserRolAssignment.assignPermanent(
                        targetUserIdentityId, RolId.of((long) i), false);
                largeList.add(ass);
            }

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.findByUserId(targetUserIdentityId)).thenReturn(largeList);
            when(readMapper.toReadDto(any(UserRolAssignment.class))).thenReturn(readAssignmentDto);

            // When
            List<ReadAssignmentDto> result = service.findByUserId(targetUserIdentityId, requesterId, requesterRolId);

            // Then
            assertThat(result).hasSize(100);
            verify(readMapper, times(100)).toReadDto(any(UserRolAssignment.class));
        }

        @Test
        @DisplayName("Debe mantener consistencia en operaciones transaccionales")
        void shouldMaintainTransactionalConsistency() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreatePermanent(createPermanentDto)).thenReturn(assignment);
            when(userRolService.assignRole(any(), any(), anyBoolean())).thenReturn(assignment);
            when(readMapper.toReadDto(assignment)).thenReturn(readAssignmentDto);

            // When
            ReadAssignmentDto result = service.savePermanent(createPermanentDto, requesterId, requesterRolId);

            // Then - verificar orden de operaciones
            var inOrder = inOrder(receptionRepository, authorizationService, writeMapper, userRolService, readMapper);
            inOrder.verify(receptionRepository).findByUserId(requesterId);
            inOrder.verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            inOrder.verify(writeMapper).fromCreatePermanent(createPermanentDto);
            inOrder.verify(userRolService).assignRole(any(), any(), anyBoolean());
            inOrder.verify(readMapper).toReadDto(assignment);
        }
    }
}