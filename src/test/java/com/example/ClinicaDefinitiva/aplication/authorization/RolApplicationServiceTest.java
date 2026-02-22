package com.example.ClinicaDefinitiva.aplication.authorization;


import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.permission.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol.RolReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.authorization.rol.RolWriteMapper;
import com.example.ClinicaDefinitiva.application.service.adminitration.authorization.RolApplicationService;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.Sector;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test Suite Completa para RolApplicationService
 *
 * Cobertura:
 * - Consultas (findById, findByRolEnum, findAll, findByEditable)
 * - Creación (createCustom)
 * - Clonación (cloneRole)
 * - Modificación de permisos (addPermission, removePermission, setPermissions)
 * - Gestión de estado (activate, deactivate, suspend, markDeleted)
 * - Eliminación (deleteById)
 * - Validaciones de autorización
 * - Manejo de errores
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RolApplicationService - Tests Completos")
class RolApplicationServiceTest {

    @Mock
    private RolReadMapper readMapper;

    @Mock
    private RolWriteMapper writeMapper;

    @Mock
    private RolRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private ReceptionRepository receptionRepository;

    @Mock
    private RolService rolService;

    @InjectMocks
    private RolApplicationService service;

    // Test Data
    private UserIdentityId requesterId;
    private RolId requesterRolId;
    private RolId targetRoleId;
    private Receptionist receptionist;
    private Rol rol;
    private ReadRolDto readRolDto;
    private CreateRolDto createRolDto;

    @BeforeEach
    void setUp() {
        requesterId = UserIdentityId.from(1L);
        requesterRolId = RolId.of(1L);
        targetRoleId = RolId.of(2L);

        // Setup Receptionist
        receptionist = mock(Receptionist.class);
        Sector sector = mock(Sector.class);
        when(sector.Value()).thenReturn("RECURSOS_HUMANOS");
        when(receptionist.getSector()).thenReturn(sector);

        // Setup Rol
        rol = new Rol(RolEnum.RECEPTIONIST, "Test Role", false, true, true, RolStatus.ACTIVE);
        rol.setId(targetRoleId);

        Set<String> permissions =new HashSet<>();
        // Setup DTOs
        readRolDto = new ReadRolDto(1l,"SECRETARIO","A",true,true,true,"",permissions);
        createRolDto = new CreateRolDto("PACIENTE","B",true,true,true);
    }

    // ===================================================================================
    // NESTED TEST CLASS: Query Operations
    // ===================================================================================
    @Nested
    @DisplayName("Query Operations")
    class QueryOperations {

        @Test
        @DisplayName("findById - Debe retornar rol cuando existe")
        void findById_WhenRolExists_ShouldReturnRol() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(readMapper.toReadDto(rol)).thenReturn(readRolDto);

            // When
            Optional<ReadRolDto> result = service.findById(targetRoleId, requesterId, requesterRolId);

            // Then
           // Assertions.asser
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(readRolDto);

            verify(repository).findById(targetRoleId);
            verify(readMapper).toReadDto(rol);
        }

        @Test
        @DisplayName("findById - Debe retornar vacío cuando no existe")
        void findById_WhenRolDoesNotExist_ShouldReturnEmpty() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.empty());

            // When
            Optional<ReadRolDto> result = service.findById(targetRoleId, requesterId, requesterRolId);

            // Then
            assertThat(result).isEmpty();

            verify(repository).findById(targetRoleId);
            verify(readMapper, never()).toReadDto(any());
        }

        @Test
        @DisplayName("findByRolEnum - Debe retornar rol cuando existe")
        void findByRolEnum_WhenRolExists_ShouldReturnRol() {
            // Given
            String rolEnumStr = "RECEPTIONIST";
            when(repository.findByRolEnum(RolEnum.RECEPTIONIST)).thenReturn(Optional.of(rol));
            when(readMapper.toReadDto(rol)).thenReturn(readRolDto);

            // When
            Optional<ReadRolDto> result = service.findByRolEnum(rolEnumStr, requesterId, requesterRolId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(readRolDto);

            verify(repository).findByRolEnum(RolEnum.RECEPTIONIST);
            verify(readMapper).toReadDto(rol);
        }

        @Test
        @DisplayName("findAll - Debe retornar página de roles")
        void findAll_ShouldReturnPageOfRoles() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            List<Rol> roles = Arrays.asList(rol, rol);
            Page<Rol> rolPage = new PageImpl<>(roles, pageable, roles.size());

            PageRolDto pageRolDto = new PageRolDto(1L, "ADMIN", "GUARDIAN", false,true,true,"ACTIVO");
            when(repository.findAll(pageable)).thenReturn(rolPage);
            when(readMapper.toPageDto(any(Rol.class))).thenReturn(pageRolDto);

            // When
            Page<PageRolDto> result = service.findAll(pageable, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);

            verify(repository).findAll(pageable);
            verify(readMapper, times(2)).toPageDto(any(Rol.class));
        }

        @Test
        @DisplayName("findByEditable - Debe retornar roles editables")
        void findByEditable_WithEditableTrue_ShouldReturnEditableRoles() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            List<Rol> roles = Arrays.asList(rol);
            Page<Rol> rolPage = new PageImpl<>(roles, pageable, roles.size());

            PageRolDto pageRolDto = new PageRolDto(1L, "ADMIN", "GUARDIAN", false,true,true,"ACTIVO");
            when(repository.findByEditable(true, pageable)).thenReturn(rolPage);
            when(readMapper.toPageDto(any(Rol.class))).thenReturn(pageRolDto);

            // When
            Page<PageRolDto> result = service.findByEditable(true, pageable, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);

            verify(repository).findByEditable(true, pageable);
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Create Custom Role
    // ===================================================================================
    @Nested
    @DisplayName("Create Custom Role Operations")
    class CreateCustomRoleOperations {

        @Test
        @DisplayName("createCustom - Debe crear rol cuando está autorizado")
        void createCustom_WhenAuthorized_ShouldCreateRole() {
            // Given
            Set<Permission> permissions = new HashSet<>();
            permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)));

            rol.setPermissions(permissions);

            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreateDto(createRolDto)).thenReturn(rol);
            when(rolService.createCustom(any(), any(), any())).thenReturn(rol);
            when(readMapper.toReadDto(rol)).thenReturn(readRolDto);

            // When
            ReadRolDto result = service.createCustom(createRolDto, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(readRolDto);

            verify(receptionRepository).findByUserId(requesterId);
            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(rolService).createCustom(rol.getRolEnum(), rol.getDescription(), rol.getPermissions());

            // Verify SecurityContext was built correctly
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            verify(authorizationService).isAuthorized(eq(requesterRolId), contextCaptor.capture());

            SecurityContext capturedContext = contextCaptor.getValue();
            assertThat(capturedContext.getAttribute("sector", String.class)).hasValue("RECURSOS_HUMANOS");
        }

        @Test
        @DisplayName("createCustom - Debe lanzar excepción cuando receptionist no existe")
        void createCustom_WhenReceptionistNotFound_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.createCustom(createRolDto, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            verify(receptionRepository).findByUserId(requesterId);
            verify(authorizationService, never()).isAuthorized(any(), any());
            verify(rolService, never()).createCustom(any(), any(), any());
        }

        @Test
        @DisplayName("createCustom - Debe lanzar excepción cuando no está autorizado")
        void createCustom_WhenNotAuthorized_ShouldThrowException() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);
            when(writeMapper.fromCreateDto(createRolDto)).thenReturn(rol);

            // When & Then
            assertThatThrownBy(() -> service.createCustom(createRolDto, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_UNAUTHORIZED_CREATION);

            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(rolService, never()).createCustom(any(), any(), any());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Clone Role
    // ===================================================================================
    @Nested
    @DisplayName("Clone Role Operations")
    class CloneRoleOperations {

        @Test
        @DisplayName("cloneRole - Debe clonar rol cuando está autorizado")
        void cloneRole_WhenAuthorized_ShouldCloneRole() {
            // Given
            String newDescription = "Cloned Role Description";

            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(rolService.cloneRole(rol, newDescription)).thenReturn(rol);
            when(readMapper.toReadDto(rol)).thenReturn(readRolDto);

            // When
            ReadRolDto result = service.cloneRole(targetRoleId, newDescription, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(readRolDto);

            verify(repository).findById(targetRoleId);
            verify(receptionRepository).findByUserId(requesterId);
            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(rolService).cloneRole(rol, newDescription);
        }

        @Test
        @DisplayName("cloneRole - Debe lanzar excepción cuando rol fuente no existe")
        void cloneRole_WhenSourceRoleNotFound_ShouldThrowException() {
            // Given
            String newDescription = "Cloned Role Description";
            when(repository.findById(targetRoleId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.cloneRole(targetRoleId, newDescription, requesterId, requesterRolId))
                    .isInstanceOf(RolNotFoundException.class);

            verify(repository).findById(targetRoleId);
            verify(rolService, never()).cloneRole(any(), any());
        }

        @Test
        @DisplayName("cloneRole - Debe lanzar excepción cuando receptionist no existe")
        void cloneRole_WhenReceptionistNotFound_ShouldThrowException() {
            // Given
            String newDescription = "Cloned Role Description";

            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.cloneRole(targetRoleId, newDescription, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            verify(rolService, never()).cloneRole(any(), any());
        }

        @Test
        @DisplayName("cloneRole - Debe lanzar excepción cuando no está autorizado")
        void cloneRole_WhenNotAuthorized_ShouldThrowException() {
            // Given
            String newDescription = "Cloned Role Description";

            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> service.cloneRole(targetRoleId, newDescription, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class);

            verify(rolService, never()).cloneRole(any(), any());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Permission Management
    // ===================================================================================
    @Nested
    @DisplayName("Permission Management Operations")
    class PermissionManagementOperations {

        private PermissionDto permissionDto;
        private Permission permission;

        @BeforeEach
        void setUp() {
            permissionDto = new PermissionDto("DELETE","PATIENT");
            permission = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        }

        @Test
        @DisplayName("addPermission - Debe agregar permiso cuando está autorizado")
        void addPermission_WhenAuthorized_ShouldAddPermission() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.toPermission(permissionDto)).thenReturn(permission);
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.addPermission(targetRoleId, permissionDto, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(repository).save(rol);
            verify(rol).addPermission(permission);
        }

        @Test
        @DisplayName("addPermission - Debe lanzar excepción cuando rol no existe")
        void addPermission_WhenRoleNotFound_ShouldThrowException() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.addPermission(targetRoleId, permissionDto, requesterId, requesterRolId))
                    .isInstanceOf(RolNotFoundException.class);

            verify(repository).findById(targetRoleId);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("removePermission - Debe remover permiso cuando está autorizado")
        void removePermission_WhenAuthorized_ShouldRemovePermission() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.toPermission(permissionDto)).thenReturn(permission);
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.removePermission(targetRoleId, permissionDto, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(repository).save(rol);
            verify(rol).removePermission(permission);
        }

        @Test
        @DisplayName("setPermissions - Debe establecer permisos cuando está autorizado")
        void setPermissions_WhenAuthorized_ShouldSetPermissions() {
            // Given
            Set<PermissionDto> permissionDtos = new HashSet<>();
            permissionDtos.add(permissionDto);

            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.toPermission(permissionDto)).thenReturn(permission);
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.setPermissions(targetRoleId, permissionDtos, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(repository).save(rol);

            ArgumentCaptor<Set<Permission>> permissionsCaptor = ArgumentCaptor.forClass(Set.class);
            verify(rol).setPermissions(permissionsCaptor.capture());

            assertThat(permissionsCaptor.getValue()).hasSize(1);
            assertThat(permissionsCaptor.getValue()).contains(permission);
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: State Management
    // ===================================================================================
    @Nested
    @DisplayName("State Management Operations")
    class StateManagementOperations {

        private String validReason;

        @BeforeEach
        void setUp() {
            validReason = "Valid reason with more than 10 characters";
        }

        @Test
        @DisplayName("activate - Debe activar rol con razón válida")
        void activate_WithValidReason_ShouldActivateRole() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.activate(targetRoleId, validReason, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(rol).activate(validReason);
            verify(repository).save(rol);
        }

        @Test
        @DisplayName("activate - Debe lanzar excepción cuando rol no existe")
        void activate_WhenRoleNotFound_ShouldThrowException() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.activate(targetRoleId, validReason, requesterId, requesterRolId))
                    .isInstanceOf(RolNotFoundException.class);

            verify(repository).findById(targetRoleId);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deactivate - Debe desactivar rol con razón válida")
        void deactivate_WithValidReason_ShouldDeactivateRole() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.deactivate(targetRoleId, validReason, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(rol).deactivate(validReason);
            verify(repository).save(rol);
        }

        @Test
        @DisplayName("suspend - Debe suspender rol con razón válida")
        void suspend_WithValidReason_ShouldSuspendRole() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.suspend(targetRoleId, validReason, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(rol).suspend(validReason);
            verify(repository).save(rol);
        }

        @Test
        @DisplayName("markDeleted - Debe marcar como eliminado con razón válida")
        void markDeleted_WithValidReason_ShouldMarkAsDeleted() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.markDeleted(targetRoleId, validReason, requesterId, requesterRolId);

            // Then
            verify(repository).findById(targetRoleId);
            verify(rol).markDeleted(validReason);
            verify(repository).save(rol);
        }

        @Test
        @DisplayName("State changes - Debe lanzar excepción cuando receptionist no existe")
        void stateChanges_WhenReceptionistNotFound_ShouldThrowException() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.empty());

            // When & Then - Test all state change methods
            assertThatThrownBy(() -> service.activate(targetRoleId, validReason, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            assertThatThrownBy(() -> service.deactivate(targetRoleId, validReason, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            assertThatThrownBy(() -> service.suspend(targetRoleId, validReason, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            assertThatThrownBy(() -> service.markDeleted(targetRoleId, validReason, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Delete Operations
    // ===================================================================================
    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperations {

        @Test
        @DisplayName("deleteById - Debe eliminar rol cuando está autorizado")
        void deleteById_WhenAuthorized_ShouldDeleteRole() {
            // Given
            when(repository.findById(requesterRolId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(repository.save(rol)).thenReturn(rol);

            // When
            service.deleteById(targetRoleId, requesterId, requesterRolId);

            // Then
            verify(repository).findById(requesterRolId);
            verify(receptionRepository).findByUserId(requesterId);
            verify(authorizationService).isAuthorized(eq(requesterRolId), any(SecurityContext.class));
            verify(rol).delete();
            verify(repository).save(rol);
        }

        @Test
        @DisplayName("deleteById - Debe lanzar excepción cuando rol no existe")
        void deleteById_WhenRoleNotFound_ShouldThrowException() {
            // Given
            when(repository.findById(requesterRolId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.deleteById(targetRoleId, requesterId, requesterRolId))
                    .isInstanceOf(RolNotFoundException.class);

            verify(repository).findById(requesterRolId);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deleteById - Debe lanzar excepción cuando no está autorizado")
        void deleteById_WhenNotAuthorized_ShouldThrowException() {
            // Given
            when(repository.findById(requesterRolId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> service.deleteById(targetRoleId, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_ROLE_DELETE_UNAUTHORIZED);

            verify(rol, never()).delete();
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deleteById - Debe lanzar excepción cuando receptionist no existe")
        void deleteById_WhenReceptionistNotFound_ShouldThrowException() {
            // Given
            when(repository.findById(requesterRolId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> service.deleteById(targetRoleId, requesterId, requesterRolId))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AuthorizationError.ERR_AUTH_SECTOR_REQUIRED);

            verify(repository, never()).save(any());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Authorization Context Validation
    // ===================================================================================
    @Nested
    @DisplayName("Authorization Context Validation")
    class AuthorizationContextValidation {

        @Test
        @DisplayName("Debe construir SecurityContext correctamente para createCustom")
        void shouldBuildCorrectSecurityContextForCreateCustom() {
            // Given
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.fromCreateDto(any())).thenReturn(rol);
            when(rolService.createCustom(any(), any(), any())).thenReturn(rol);
            when(readMapper.toReadDto(any())).thenReturn(readRolDto);

            // When
            service.createCustom(createRolDto, requesterId, requesterRolId);

            // Then
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            verify(authorizationService).isAuthorized(eq(requesterRolId), contextCaptor.capture());

            SecurityContext context = contextCaptor.getValue();
            assertThat(context.getPermission().getResource())
                    .isEqualTo(ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE));
            assertThat(context.getPermission().getAction())
                    .isEqualTo(ActionCatalog.of(ActionCatalog.BasicAction.CREATE));
            assertThat(context.getRequestingUserId()).isEqualTo(requesterId);
            assertThat(context.getAttribute("sector", String.class)).hasValue("RECURSOS_HUMANOS");
        }

        @Test
        @DisplayName("Debe construir SecurityContext correctamente para cloneRole")
        void shouldBuildCorrectSecurityContextForCloneRole() {
            // Given
            String newDescription = "New Description";
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(rolService.cloneRole(any(), any())).thenReturn(rol);
            when(readMapper.toReadDto(any())).thenReturn(readRolDto);

            // When
            service.cloneRole(targetRoleId, newDescription, requesterId, requesterRolId);

            // Then
            ArgumentCaptor<SecurityContext> contextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
            verify(authorizationService).isAuthorized(eq(requesterRolId), contextCaptor.capture());

            SecurityContext context = contextCaptor.getValue();
            assertThat(context.getPermission().getAction())
                    .isEqualTo(ActionCatalog.of(ActionCatalog.BasicAction.CLONE_ROLE));
            assertThat(context.getAttribute("sector", String.class)).hasValue("RECURSOS_HUMANOS");
            assertThat(context.getAttribute("resourceId", Long.class)).hasValue(targetRoleId.getValue());
        }
    }

    // ===================================================================================
    // NESTED TEST CLASS: Edge Cases
    // ===================================================================================
    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("Debe manejar correctamente rol sin permisos")
        void shouldHandleRoleWithoutPermissions() {
            // Given
            Rol emptyPermissionsRol = new Rol(RolEnum.PATIENT, "Patient Role", true, false, false, RolStatus.ACTIVE);
            emptyPermissionsRol.setId(targetRoleId);

            when(repository.findById(targetRoleId)).thenReturn(Optional.of(emptyPermissionsRol));
            when(readMapper.toReadDto(emptyPermissionsRol)).thenReturn(readRolDto);

            // When
            Optional<ReadRolDto> result = service.findById(targetRoleId, requesterId, requesterRolId);

            // Then
            assertThat(result).isPresent();
            verify(readMapper).toReadDto(emptyPermissionsRol);
        }

        @Test
        @DisplayName("Debe manejar correctamente múltiples llamadas concurrentes")
        void shouldHandleConcurrentCalls() {
            // Given
            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(readMapper.toReadDto(rol)).thenReturn(readRolDto);

            // When - Simular múltiples llamadas
            Optional<ReadRolDto> result1 = service.findById(targetRoleId, requesterId, requesterRolId);
            Optional<ReadRolDto> result2 = service.findById(targetRoleId, requesterId, requesterRolId);
            Optional<ReadRolDto> result3 = service.findById(targetRoleId, requesterId, requesterRolId);

            // Then
            assertThat(result1).isPresent();
            assertThat(result2).isPresent();
            assertThat(result3).isPresent();

            verify(repository, times(3)).findById(targetRoleId);
            verify(readMapper, times(3)).toReadDto(rol);
        }

        @Test
        @DisplayName("Debe manejar correctamente página vacía")
        void shouldHandleEmptyPage() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Rol> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(repository.findAll(pageable)).thenReturn(emptyPage);

            // When
            Page<PageRolDto> result = service.findAll(pageable, requesterId, requesterRolId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();

            verify(readMapper, never()).toPageDto(any());
        }

        @Test
        @DisplayName("Debe manejar correctamente valores nulos en permisos")
        void shouldHandleNullPermissions() {
            // Given
            PermissionDto permissionDto = new PermissionDto("","");

            when(repository.findById(targetRoleId)).thenReturn(Optional.of(rol));
            when(receptionRepository.findByUserId(requesterId)).thenReturn(Optional.of(receptionist));
            when(authorizationService.isAuthorized(eq(requesterRolId), any(SecurityContext.class))).thenReturn(true);
            when(writeMapper.toPermission(permissionDto)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> service.addPermission(targetRoleId, permissionDto, requesterId, requesterRolId))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
