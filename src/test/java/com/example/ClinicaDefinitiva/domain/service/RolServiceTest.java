package com.example.ClinicaDefinitiva.domain.service;


import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests para RolService - Servicio de Dominio
 *
 * IMPORTANTE: Este servicio encapsula reglas de negocio que REQUIEREN
 * colaboración con repositorios (validación de unicidad).
 *
 * La diferencia con tests de agregados:
 * - Agregados: Reglas que el agregado puede validar por sí solo
 * - Servicios de Dominio: Reglas que requieren consultar otros agregados/repositorio
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RolService - Domain Service Tests")
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Set<Permission> permissions;
    private Permission permission1;
    private Permission permission2;

    @BeforeEach
    void setUp() {
        permission1 = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        permission2 = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT));

        permissions = new HashSet<>();
        permissions.add(permission1);
        permissions.add(permission2);
    }

    // ===================================================================================
    // NESTED: Create Custom Role
    // ===================================================================================
    @Nested
    @DisplayName("Create Custom Role Operations")
    class CreateCustomRoleOperations {

        @Test
        @DisplayName("createCustom debe crear rol cuando descripción es única")
        void createCustom_WhenDescriptionIsUnique_ShouldCreateRole() {
            // Given
            String uniqueDescription = "Unique Custom Role";
            when(rolRepository.existsByDescription(uniqueDescription)).thenReturn(false);

            Rol expectedRol = new Rol(
                    RolEnum.RECEPTIONIST,
                    uniqueDescription,
                    false,
                    true,
                    true,
                    RolStatus.ACTIVE
            );
            when(rolRepository.save(any(Rol.class))).thenReturn(expectedRol);

            // When
            Rol result = rolService.createCustom(
                    RolEnum.RECEPTIONIST,
                    uniqueDescription,
                    permissions
            );

            // Then
            assertThat(result).isNotNull();
            verify(rolRepository).existsByDescription(uniqueDescription);
            verify(rolRepository).save(any(Rol.class));
        }

        @Test
        @DisplayName("createCustom debe lanzar excepción cuando descripción ya existe")
        void createCustom_WhenDescriptionExists_ShouldThrowException() {
            // Given
            String duplicateDescription = "Duplicate Role";
            when(rolRepository.existsByDescription(duplicateDescription)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> rolService.createCustom(
                    RolEnum.RECEPTIONIST,
                    duplicateDescription,
                    permissions
            ))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_DUPLICATE_DESCRIPTION);

            // Verificar que NO se llamó save
            verify(rolRepository).existsByDescription(duplicateDescription);
            verify(rolRepository, never()).save(any());
        }

        @Test
        @DisplayName("createCustom debe crear rol con permisos correctos")
        void createCustom_ShouldCreateRoleWithCorrectPermissions() {
            // Given
            String description = "Custom Role with Permissions";
            when(rolRepository.existsByDescription(description)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.DENTIST, description, permissions);

            // Then
            Rol savedRol = rolCaptor.getValue();
            assertThat(savedRol.getPermissions()).hasSize(2);
            assertThat(savedRol.hasPermission(permission1)).isTrue();
            assertThat(savedRol.hasPermission(permission2)).isTrue();
        }

        @Test
        @DisplayName("createCustom debe crear rol con propiedades custom correctas")
        void createCustom_ShouldCreateRoleWithCorrectCustomProperties() {
            // Given
            String description = "Test Custom Role";
            when(rolRepository.existsByDescription(description)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.RECEPTIONIST, description, permissions);

            // Then
            Rol savedRol = rolCaptor.getValue();
            assertThat(savedRol.getRolEnum()).isEqualTo(RolEnum.RECEPTIONIST);
            assertThat(savedRol.getDescription()).isEqualTo(description);
            assertThat(savedRol.isDefault()).isFalse();
            assertThat(savedRol.isEditable()).isTrue();
            assertThat(savedRol.isDeletable()).isTrue();
            assertThat(savedRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
        }

        @Test
        @DisplayName("createCustom debe hacer copia defensiva de permisos")
        void createCustom_ShouldMakeDefensiveCopyOfPermissions() {
            // Given
            String description = "Test Role";
            Set<Permission> externalPermissions = new HashSet<>(permissions);
            when(rolRepository.existsByDescription(description)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.PATIENT, description, externalPermissions);

            // Modificar el set externo DESPUÉS de la creación
            Permission newPermission = Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
            externalPermissions.add(newPermission);

            // Then - El rol guardado NO debe verse afectado
            Rol savedRol = rolCaptor.getValue();
            assertThat(savedRol.getPermissions()).hasSize(2);
            assertThat(savedRol.hasPermission(newPermission)).isFalse();
        }

        @Test
        @DisplayName("createCustom con permisos vacíos debe crear rol sin permisos")
        void createCustom_WithEmptyPermissions_ShouldCreateRoleWithoutPermissions() {
            // Given
            String description = "Role without permissions";
            Set<Permission> emptyPermissions = new HashSet<>();
            when(rolRepository.existsByDescription(description)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.GUARDIAN, description, emptyPermissions);

            // Then
            Rol savedRol = rolCaptor.getValue();
            assertThat(savedRol.getPermissions()).isEmpty();
        }

        @Test
        @DisplayName("createCustom debe validar unicidad ANTES de crear el rol")
        void createCustom_ShouldValidateUniquenessBeforeCreating() {
            // Given
            String description = "Test Role";
            when(rolRepository.existsByDescription(description)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> rolService.createCustom(
                    RolEnum.RECEPTIONIST, description, permissions
            ))
                    .isInstanceOf(BusinessRuleViolationException.class);

            // Verificar orden de ejecución
            verify(rolRepository).existsByDescription(description);
            verify(rolRepository, never()).save(any());
        }
    }

    // ===================================================================================
    // NESTED: Clone Role
    // ===================================================================================
    @Nested
    @DisplayName("Clone Role Operations")
    class CloneRoleOperations {

        private Rol sourceRole;

        @BeforeEach
        void setUp() {
            sourceRole = new Rol(
                    RolEnum.DENTIST,
                    "Original Dentist Role",
                    false,
                    true,
                    true,
                    RolStatus.ACTIVE
            );
            sourceRole.addPermission(permission1);
            sourceRole.addPermission(permission2);
        }

        @Test
        @DisplayName("cloneRole debe clonar rol cuando nueva descripción es única")
        void cloneRole_WhenNewDescriptionIsUnique_ShouldCloneRole() {
            // Given
            String newDescription = "Cloned Dentist Role";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            Rol cloned = rolService.cloneRole(sourceRole, newDescription);

            // Then
            assertThat(cloned).isNotNull();
            assertThat(cloned.getDescription()).isEqualTo(newDescription);
            verify(rolRepository).existsByDescription(newDescription);
            verify(rolRepository).save(any(Rol.class));
        }

        @Test
        @DisplayName("cloneRole debe lanzar excepción cuando nueva descripción ya existe")
        void cloneRole_WhenNewDescriptionExists_ShouldThrowException() {
            // Given
            String duplicateDescription = "Existing Role";
            when(rolRepository.existsByDescription(duplicateDescription)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> rolService.cloneRole(sourceRole, duplicateDescription))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_DUPLICATE_DESCRIPTION);

            verify(rolRepository).existsByDescription(duplicateDescription);
            verify(rolRepository, never()).save(any());
        }

        @Test
        @DisplayName("cloneRole debe copiar todos los permisos del rol fuente")
        void cloneRole_ShouldCopyAllPermissionsFromSource() {
            // Given
            String newDescription = "Clone with Permissions";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.cloneRole(sourceRole, newDescription);

            // Then
            Rol clonedRol = rolCaptor.getValue();
            assertThat(clonedRol.getPermissions()).hasSize(2);
            assertThat(clonedRol.hasPermission(permission1)).isTrue();
            assertThat(clonedRol.hasPermission(permission2)).isTrue();
        }

        @Test
        @DisplayName("cloneRole debe crear copia independiente de permisos")
        void cloneRole_ShouldCreateIndependentCopyOfPermissions() {
            // Given
            String newDescription = "Independent Clone";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.cloneRole(sourceRole, newDescription);

            // Modificar el source DESPUÉS del clonado
            Permission newPermission = Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
            sourceRole.addPermission(newPermission);

            // Then - El clon NO debe verse afectado
            Rol clonedRol = rolCaptor.getValue();
            assertThat(clonedRol.getPermissions()).hasSize(2);
            assertThat(clonedRol.hasPermission(newPermission)).isFalse();
        }

        @Test
        @DisplayName("cloneRole debe copiar RolEnum del source")
        void cloneRole_ShouldCopyRolEnumFromSource() {
            // Given
            String newDescription = "Clone Test";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.cloneRole(sourceRole, newDescription);

            // Then
            Rol clonedRol = rolCaptor.getValue();
            assertThat(clonedRol.getRolEnum()).isEqualTo(sourceRole.getRolEnum());
        }

        @Test
        @DisplayName("cloneRole SIEMPRE debe crear rol NO default, editable y deletable")
        void cloneRole_ShouldAlwaysCreateNonDefaultEditableDeletableRole() {
            // Given
            Rol defaultRole = new Rol(
                    RolEnum.PATIENT,
                    "Default System Role",
                    true,   // default
                    false,  // NO editable
                    false,  // NO deletable
                    RolStatus.ACTIVE
            );
            String newDescription = "Clone of Default";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.cloneRole(defaultRole, newDescription);

            // Then
            Rol clonedRol = rolCaptor.getValue();
            assertThat(clonedRol.isDefault()).isFalse();
            assertThat(clonedRol.isEditable()).isTrue();
            assertThat(clonedRol.isDeletable()).isTrue();
        }

        @Test
        @DisplayName("cloneRole debe crear rol con estado ACTIVE")
        void cloneRole_ShouldCreateRoleWithActiveStatus() {
            // Given
            Rol inactiveRole = new Rol(
                    RolEnum.GUARDIAN,
                    "Inactive Role",
                    false,
                    true,
                    true,
                    RolStatus.INACTIVE
            );
            String newDescription = "Clone of Inactive";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.cloneRole(inactiveRole, newDescription);

            // Then
            Rol clonedRol = rolCaptor.getValue();
            assertThat(clonedRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
        }

        @Test
        @DisplayName("cloneRole de rol sin permisos debe funcionar")
        void cloneRole_OfRoleWithoutPermissions_ShouldWork() {
            // Given
            Rol emptyRole = new Rol(
                    RolEnum.RECEPTIONIST,
                    "Empty Role",
                    false,
                    true,
                    true,
                    RolStatus.ACTIVE
            );
            String newDescription = "Clone of Empty";
            when(rolRepository.existsByDescription(newDescription)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.cloneRole(emptyRole, newDescription);

            // Then
            Rol clonedRol = rolCaptor.getValue();
            assertThat(clonedRol.getPermissions()).isEmpty();
        }
    }

    // ===================================================================================
    // NESTED: Validation Logic
    // ===================================================================================
    @Nested
    @DisplayName("Validation Logic")
    class ValidationLogic {

        @Test
        @DisplayName("Validación de unicidad debe ser case-sensitive")
        void uniquenessValidation_ShouldBeCaseSensitive() {
            // Given
            String description = "Test Role";
            when(rolRepository.existsByDescription(description)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> rolService.createCustom(
                    RolEnum.DENTIST, description, permissions
            ))
                    .isInstanceOf(BusinessRuleViolationException.class);

            // Verificar que se buscó exactamente con la descripción dada
            verify(rolRepository).existsByDescription(description);
        }

        @Test
        @DisplayName("Validación de unicidad debe llamarse exactamente una vez")
        void uniquenessValidation_ShouldBeCalledExactlyOnce() {
            // Given
            String description = "Unique Role";
            when(rolRepository.existsByDescription(description)).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.PATIENT, description, permissions);

            // Then
            verify(rolRepository, times(1)).existsByDescription(description);
        }

        @Test
        @DisplayName("Fallo en save no debe dejar estado inconsistente")
        void saveFailing_ShouldNotLeaveInconsistentState() {
            // Given
            String description = "Test Role";
            when(rolRepository.existsByDescription(description)).thenReturn(false);
            when(rolRepository.save(any(Rol.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // When & Then
            assertThatThrownBy(() -> rolService.createCustom(
                    RolEnum.RECEPTIONIST, description, permissions
            ))
                    .isInstanceOf(RuntimeException.class);

            // Verificar que se intentó validar y guardar
            verify(rolRepository).existsByDescription(description);
            verify(rolRepository).save(any(Rol.class));
        }
    }

    // ===================================================================================
    // NESTED: Edge Cases
    // ===================================================================================
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("createCustom con descripción muy larga")
        void createCustom_WithVeryLongDescription_ShouldWork() {
            // Given
            String longDescription = "A".repeat(255); // Assuming max length
            when(rolRepository.existsByDescription(longDescription)).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

            // When & Then
            assertThatCode(() -> rolService.createCustom(
                    RolEnum.DENTIST, longDescription, permissions
            )).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("createCustom con muchos permisos")
        void createCustom_WithManyPermissions_ShouldWork() {
            // Given
            Set<Permission> manyPermissions = new HashSet<>();
            for (ResourceCatalog.BasicResource resource : ResourceCatalog.BasicResource.values()) {
                manyPermissions.add(Permission.read(ResourceCatalog.of(resource)));
                manyPermissions.add(Permission.create(ResourceCatalog.of(resource)));
            }

            String description = "Role with many permissions";
            when(rolRepository.existsByDescription(description)).thenReturn(false);

            ArgumentCaptor<Rol> rolCaptor = ArgumentCaptor.forClass(Rol.class);
            when(rolRepository.save(rolCaptor.capture())).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.RECEPTIONIST, description, manyPermissions);

            // Then
            Rol savedRol = rolCaptor.getValue();
            assertThat(savedRol.getPermissions().size()).isGreaterThan(10);
        }

        @Test
        @DisplayName("cloneRole múltiples veces debe generar descripciones únicas")
        void cloneRole_MultipleTimes_ShouldHandleUniqueDescriptions() {
            // Given
            Rol source = new Rol(RolEnum.DENTIST, "Original", false, true, true, RolStatus.ACTIVE);

            when(rolRepository.existsByDescription("Clone 1")).thenReturn(false);
            when(rolRepository.existsByDescription("Clone 2")).thenReturn(false);
            when(rolRepository.existsByDescription("Clone 3")).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

            // When
            Rol clone1 = rolService.cloneRole(source, "Clone 1");
            Rol clone2 = rolService.cloneRole(source, "Clone 2");
            Rol clone3 = rolService.cloneRole(source, "Clone 3");

            // Then
            assertThat(clone1.getDescription()).isEqualTo("Clone 1");
            assertThat(clone2.getDescription()).isEqualTo("Clone 2");
            assertThat(clone3.getDescription()).isEqualTo("Clone 3");
        }

        @Test
        @DisplayName("createCustom debe funcionar para todos los RolEnum")
        void createCustom_ShouldWorkForAllRolEnums() {
            // Given
            when(rolRepository.existsByDescription(anyString())).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

            // When & Then
            for (RolEnum rolEnum : RolEnum.values()) {
                assertThatCode(() -> rolService.createCustom(
                        rolEnum,
                        "Custom " + rolEnum.name(),
                        permissions
                )).doesNotThrowAnyException();
            }
        }
    }

    // ===================================================================================
    // NESTED: Integration with Repository
    // ===================================================================================
    @Nested
    @DisplayName("Integration with Repository")
    class IntegrationWithRepository {

        @Test
        @DisplayName("Debe llamar a repository.save exactamente una vez por creación")
        void shouldCallRepositorySaveExactlyOnce() {
            // Given
            String description = "Test Role";
            when(rolRepository.existsByDescription(description)).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.PATIENT, description, permissions);

            // Then
            verify(rolRepository, times(1)).save(any(Rol.class));
        }

        @Test
        @DisplayName("Debe retornar exactamente lo que el repositorio retorna")
        void shouldReturnExactlyWhatRepositoryReturns() {
            // Given
            String description = "Test Role";
            Rol expectedRol = new Rol(
                    RolEnum.DENTIST,
                    description,
                    false,
                    true,
                    true,
                    RolStatus.ACTIVE
            );

            when(rolRepository.existsByDescription(description)).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenReturn(expectedRol);

            // When
            Rol result = rolService.createCustom(RolEnum.DENTIST, description, permissions);

            // Then
            assertThat(result).isSameAs(expectedRol);
        }

        @Test
        @DisplayName("Orden de llamadas: primero existsByDescription, luego save")
        void orderOfCalls_ShouldCheckExistenceBeforeSaving() {
            // Given
            String description = "Test Role";
            when(rolRepository.existsByDescription(description)).thenReturn(false);
            when(rolRepository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

            // When
            rolService.createCustom(RolEnum.GUARDIAN, description, permissions);

            // Then
            var inOrder = inOrder(rolRepository);
            inOrder.verify(rolRepository).existsByDescription(description);
            inOrder.verify(rolRepository).save(any(Rol.class));
        }
    }
}
