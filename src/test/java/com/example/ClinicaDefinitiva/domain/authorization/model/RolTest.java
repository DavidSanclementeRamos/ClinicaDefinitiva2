package com.example.ClinicaDefinitiva.domain.authorization.model;



import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests para el Agregado Rol
 *
 * IMPORTANTE: Los agregados son el CORAZÓN del dominio. Aquí están las reglas de negocio críticas.
 * Estos tests son MÁS IMPORTANTES que los de la capa de aplicación porque validan:
 * - Invariantes del dominio
 * - Reglas de negocio core
 * - Estado interno consistente
 * - Transiciones de estado válidas
 */
@DisplayName("Rol - Domain Aggregate Tests")
class RolTest {

    private Rol editableRol;
    private Rol systemRol;
    private Permission permission1;
    private Permission permission2;

    @BeforeEach
    void setUp() {
        // Rol editable - custom
        editableRol = new Rol(
                RolEnum.RECEPTIONIST,
                "Custom Receptionist Role",
                false,  // no default
                true,   // editable
                true,   // deletable
                RolStatus.ACTIVE
        );

        // Rol del sistema - no editable
        systemRol = new Rol(
                RolEnum.DENTIST,
                "System Dentist Role",
                true,   // default
                false,  // NO editable
                false,  // NO deletable
                RolStatus.ACTIVE
        );

        permission1 = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        permission2 = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT));
    }

    // ===================================================================================
    // NESTED: Constructor y Estado Inicial
    // ===================================================================================
    @Nested
    @DisplayName("Constructor y Estado Inicial")
    class ConstructorAndInitialState {

        @Test
        @DisplayName("Constructor debe inicializar todos los campos correctamente")
        void constructor_ShouldInitializeAllFieldsCorrectly() {
            // When
            Rol rol = new Rol(
                    RolEnum.PATIENT,
                    "Patient Role",
                    true,
                    false,
                    false,
                    RolStatus.ACTIVE
            );

            // Then
            assertThat(rol.getRolEnum()).isEqualTo(RolEnum.PATIENT);
            assertThat(rol.getDescription()).isEqualTo("Patient Role");
            assertThat(rol.isDefault()).isTrue();
            assertThat(rol.isEditable()).isFalse();
            assertThat(rol.isDeletable()).isFalse();
            assertThat(rol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
            assertThat(rol.getPermissions()).isEmpty();
        }

        @Test
        @DisplayName("Constructor debe inicializar Set de permisos vacío pero no null")
        void constructor_ShouldInitializeEmptyPermissionsSet() {
            // When
            Rol rol = new Rol(RolEnum.GUARDIAN, "Guardian", false, true, true, RolStatus.ACTIVE);

            // Then
            assertThat(rol.getPermissions())
                    .isNotNull()
                    .isEmpty();
        }

        @ParameterizedTest
        @EnumSource(RolEnum.class)
        @DisplayName("Constructor debe aceptar todos los tipos de RolEnum")
        void constructor_ShouldAcceptAllRolEnumTypes(RolEnum rolEnum) {
            // When
            Rol rol = new Rol(rolEnum, "Test Role", false, true, true, RolStatus.ACTIVE);

            // Then
            assertThat(rol.getRolEnum()).isEqualTo(rolEnum);
        }

        @ParameterizedTest
        @EnumSource(RolStatus.class)
        @DisplayName("Constructor debe aceptar todos los estados")
        void constructor_ShouldAcceptAllRolStatusTypes(RolStatus status) {
            // When
            Rol rol = new Rol(RolEnum.RECEPTIONIST, "Test", false, true, true, status);

            // Then
            assertThat(rol.getStatusRol()).isEqualTo(status);
        }
    }

    // ===================================================================================
    // NESTED: Gestión de Permisos
    // ===================================================================================
    @Nested
    @DisplayName("Gestión de Permisos")
    class PermissionManagement {

        @Test
        @DisplayName("addPermission debe agregar permiso cuando el rol es editable")
        void addPermission_WhenEditable_ShouldAddPermission() {
            // When
            editableRol.addPermission(permission1);

            // Then
            assertThat(editableRol.hasPermission(permission1)).isTrue();
            assertThat(editableRol.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("addPermission debe lanzar excepción cuando el rol NO es editable")
        void addPermission_WhenNotEditable_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> systemRol.addPermission(permission1))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_SYSTEM_NOT_EDITABLE);

            // Verificar que NO se agregó
            assertThat(systemRol.hasPermission(permission1)).isFalse();
        }

        @Test
        @DisplayName("addPermission debe permitir múltiples permisos distintos")
        void addPermission_ShouldAllowMultipleDistinctPermissions() {
            // When
            editableRol.addPermission(permission1);
            editableRol.addPermission(permission2);

            // Then
            assertThat(editableRol.getPermissions()).hasSize(2);
            assertThat(editableRol.hasPermission(permission1)).isTrue();
            assertThat(editableRol.hasPermission(permission2)).isTrue();
        }

        @Test
        @DisplayName("addPermission NO debe duplicar permisos (Set behavior)")
        void addPermission_ShouldNotDuplicatePermissions() {
            // When
            editableRol.addPermission(permission1);
            editableRol.addPermission(permission1);
            editableRol.addPermission(permission1);

            // Then
            assertThat(editableRol.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("removePermission debe remover permiso cuando el rol es editable")
        void removePermission_WhenEditable_ShouldRemovePermission() {
            // Given
            editableRol.addPermission(permission1);
            editableRol.addPermission(permission2);

            // When
            editableRol.removePermission(permission1);

            // Then
            assertThat(editableRol.hasPermission(permission1)).isFalse();
            assertThat(editableRol.hasPermission(permission2)).isTrue();
            assertThat(editableRol.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("removePermission debe lanzar excepción cuando el rol NO es editable")
        void removePermission_WhenNotEditable_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> systemRol.removePermission(permission1))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_SYSTEM_NOT_EDITABLE);
        }

        @Test
        @DisplayName("removePermission debe ser idempotente (no error si no existe)")
        void removePermission_WhenPermissionDoesNotExist_ShouldBeIdempotent() {
            // When & Then - NO debe lanzar excepción
            assertThatCode(() -> editableRol.removePermission(permission1))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("setPermissions debe reemplazar todos los permisos")
        void setPermissions_ShouldReplaceAllPermissions() {
            // Given
            editableRol.addPermission(permission1);
            Set<Permission> newPermissions = new HashSet<>();
            newPermissions.add(permission2);

            // When
            editableRol.setPermissions(newPermissions);

            // Then
            assertThat(editableRol.getPermissions()).hasSize(1);
            assertThat(editableRol.hasPermission(permission1)).isFalse();
            assertThat(editableRol.hasPermission(permission2)).isTrue();
        }

        @Test
        @DisplayName("setPermissions debe lanzar excepción cuando NO es editable")
        void setPermissions_WhenNotEditable_ShouldThrowException() {
            // Given
            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission1);

            // When & Then
            assertThatThrownBy(() -> systemRol.setPermissions(permissions))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_SYSTEM_NOT_EDITABLE);
        }

        @Test
        @DisplayName("setPermissions debe crear copia defensiva (no compartir referencia)")
        void setPermissions_ShouldCreateDefensiveCopy() {
            // Given
            Set<Permission> externalSet = new HashSet<>();
            externalSet.add(permission1);

            // When
            editableRol.setPermissions(externalSet);
            externalSet.add(permission2); // Modificar el set externo

            // Then - El rol NO debe verse afectado
            assertThat(editableRol.getPermissions()).hasSize(1);
            assertThat(editableRol.hasPermission(permission2)).isFalse();
        }

        @Test
        @DisplayName("getPermissions debe retornar copia defensiva")
        void getPermissions_ShouldReturnDefensiveCopy() {
            // Given
            editableRol.addPermission(permission1);

            // When
            Set<Permission> permissions = editableRol.getPermissions();
            permissions.add(permission2); // Intentar modificar

            // Then - El rol NO debe verse afectado
            assertThat(editableRol.getPermissions()).hasSize(1);
            assertThat(editableRol.hasPermission(permission2)).isFalse();
        }

        @Test
        @DisplayName("hasPermission debe retornar false para permiso no existente")
        void hasPermission_WhenPermissionDoesNotExist_ShouldReturnFalse() {
            // When & Then
            assertThat(editableRol.hasPermission(permission1)).isFalse();
        }

        @Test
        @DisplayName("hasPermission debe retornar true para permiso existente")
        void hasPermission_WhenPermissionExists_ShouldReturnTrue() {
            // Given
            editableRol.addPermission(permission1);

            // When & Then
            assertThat(editableRol.hasPermission(permission1)).isTrue();
        }
    }

    // ===================================================================================
    // NESTED: Gestión de Estado (State Transitions)
    // ===================================================================================
    @Nested
    @DisplayName("Gestión de Estado - Transiciones")
    class StateTransitions {

        @Test
        @DisplayName("activate debe cambiar estado a ACTIVE con razón válida")
        void activate_WithValidReason_ShouldChangeToActive() {
            // Given
            editableRol.setStatusRol(RolStatus.INACTIVE);
            String reason = "Reactivating role for new project";

            // When
            editableRol.activate(reason);

            // Then
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "short"})
        @DisplayName("activate debe lanzar excepción con razón inválida (< 10 caracteres)")
        void activate_WithInvalidReason_ShouldThrowException(String invalidReason) {
            // When & Then
            assertThatThrownBy(() -> editableRol.activate(invalidReason))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_DELETE_REASON_REQUIRED);
        }

        @Test
        @DisplayName("activate debe lanzar excepción con razón null")
        void activate_WithNullReason_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> editableRol.activate(null))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_DELETE_REASON_REQUIRED);
        }

        @Test
        @DisplayName("deactivate debe cambiar estado a INACTIVE")
        void deactivate_WithValidReason_ShouldChangeToInactive() {
            // Given
            String reason = "Deactivating due to organizational changes";

            // When
            editableRol.deactivate(reason);

            // Then
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.INACTIVE);
        }

        @Test
        @DisplayName("suspend debe cambiar estado a SUSPENDED")
        void suspend_WithValidReason_ShouldChangeToSuspended() {
            // Given
            String reason = "Suspending pending security review";

            // When
            editableRol.suspend(reason);

            // Then
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.SUSPENDED);
        }

        @Test
        @DisplayName("markDeleted debe cambiar estado a DELETED cuando es deletable")
        void markDeleted_WhenDeletable_ShouldChangeToDeleted() {
            // Given
            String reason = "Deleting obsolete role definition";

            // When
            editableRol.markDeleted(reason);

            // Then
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.DELETED);
        }

        @Test
        @DisplayName("markDeleted debe lanzar excepción cuando NO es deletable")
        void markDeleted_WhenNotDeletable_ShouldThrowException() {
            // Given
            String reason = "Attempting to delete system role";

            // When & Then
            assertThatThrownBy(() -> systemRol.markDeleted(reason))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_SYSTEM_NOT_DELETABLE);

            // Verificar que el estado NO cambió
            assertThat(systemRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
        }

        @Test
        @DisplayName("Múltiples transiciones de estado deben funcionar correctamente")
        void multipleStateTransitions_ShouldWorkCorrectly() {
            // Given
            String reason = "Valid reason for state change";

            // When & Then
            editableRol.suspend(reason);
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.SUSPENDED);

            editableRol.activate(reason);
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);

            editableRol.deactivate(reason);
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.INACTIVE);

            editableRol.activate(reason);
            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
        }
    }

    // ===================================================================================
    // NESTED: Delete Operations
    // ===================================================================================
    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperations {

        @Test
        @DisplayName("delete debe ejecutarse sin error cuando es deletable")
        void delete_WhenDeletable_ShouldExecuteWithoutError() {
            // When & Then
            assertThatCode(() -> editableRol.delete())
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("delete debe lanzar excepción cuando NO es deletable")
        void delete_WhenNotDeletable_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> systemRol.delete())
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", RolError.ERR_ROL_SYSTEM_NOT_DELETABLE);
        }

        @Test
        @DisplayName("delete vs markDeleted - comportamiento diferente")
        void delete_VsMarkDeleted_ShouldHaveDifferentBehavior() {
            // Given
            Rol rol1 = new Rol(RolEnum.RECEPTIONIST, "Role 1", false, true, true, RolStatus.ACTIVE);
            Rol rol2 = new Rol(RolEnum.RECEPTIONIST, "Role 2", false, true, true, RolStatus.ACTIVE);
            String reason = "Valid deletion reason";

            // When
            rol1.delete(); // No cambia estado
            rol2.markDeleted(reason); // Cambia estado a DELETED

            // Then
            assertThat(rol1.getStatusRol()).isEqualTo(RolStatus.ACTIVE); // No cambia
            assertThat(rol2.getStatusRol()).isEqualTo(RolStatus.DELETED); // Cambia
        }
    }

    // ===================================================================================
    // NESTED: Factory Methods
    // ===================================================================================
    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethods {

        @Test
        @DisplayName("createCustom debe crear rol custom correctamente")
        void createCustom_ShouldCreateCustomRoleCorrectly() {
            // Given
            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission1);
            permissions.add(permission2);

            // When
            Rol customRole = editableRol.createCustom(
                    RolEnum.RECEPTIONIST,
                    "Custom Reception Role",
                    permissions
            );

            // Then
            assertThat(customRole.getRolEnum()).isEqualTo(RolEnum.RECEPTIONIST);
            assertThat(customRole.getDescription()).isEqualTo("Custom Reception Role");
            assertThat(customRole.isDefault()).isFalse();
            assertThat(customRole.isEditable()).isTrue();
            assertThat(customRole.isDeletable()).isTrue();
            assertThat(customRole.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
            assertThat(customRole.getPermissions()).hasSize(2);
        }

        @Test
        @DisplayName("createCustom debe crear copia defensiva de permisos")
        void createCustom_ShouldCreateDefensiveCopyOfPermissions() {
            // Given
            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission1);

            // When
            Rol customRole = editableRol.createCustom(RolEnum.RECEPTIONIST, "Test", permissions);
            permissions.add(permission2); // Modificar el set original

            // Then - El rol custom NO debe verse afectado
            assertThat(customRole.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("cloneRole debe clonar rol con nueva descripción")
        void cloneRole_ShouldCloneRoleWithNewDescription() {
            // Given
            editableRol.addPermission(permission1);
            editableRol.addPermission(permission2);

            // When
            Rol cloned = editableRol.cloneRole(editableRol, "Cloned Role Description");

            // Then
            assertThat(cloned.getRolEnum()).isEqualTo(editableRol.getRolEnum());
            assertThat(cloned.getDescription()).isEqualTo("Cloned Role Description");
            assertThat(cloned.isDefault()).isFalse();
            assertThat(cloned.isEditable()).isTrue();
            assertThat(cloned.isDeletable()).isTrue();
            assertThat(cloned.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
            assertThat(cloned.getPermissions()).hasSize(2);
        }

        @Test
        @DisplayName("cloneRole debe crear copia independiente de permisos")
        void cloneRole_ShouldCreateIndependentCopyOfPermissions() {
            // Given
            editableRol.addPermission(permission1);

            // When
            Rol cloned = editableRol.cloneRole(editableRol, "Cloned");

            // Modificar el original
            Permission newPermission = Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
            editableRol.addPermission(newPermission);

            // Then - El clonado NO debe verse afectado
            assertThat(editableRol.getPermissions()).hasSize(2);
            assertThat(cloned.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("cloneRole de un rol default debe crear rol NO default")
        void cloneRole_FromDefaultRole_ShouldCreateNonDefaultRole() {
            // Given
            Rol defaultRole = new Rol(RolEnum.DENTIST, "Default Dentist", true, false, false, RolStatus.ACTIVE);

            // When
            Rol cloned = defaultRole.cloneRole(defaultRole, "Cloned Dentist");

            // Then
            assertThat(defaultRole.isDefault()).isTrue();
            assertThat(cloned.isDefault()).isFalse();
        }
    }

    // ===================================================================================
    // NESTED: Invariantes y Reglas de Negocio
    // ===================================================================================
    @Nested
    @DisplayName("Invariantes y Reglas de Negocio")
    class InvariantsAndBusinessRules {

        @Test
        @DisplayName("INVARIANTE: Rol de sistema NO debe ser editable ni deletable")
        void invariant_SystemRoleShouldNotBeEditableOrDeletable() {
            // Given
            Rol systemRole = new Rol(
                    RolEnum.DENTIST,
                    "System Dentist",
                    true,   // default
                    false,  // NO editable
                    false,  // NO deletable
                    RolStatus.ACTIVE
            );

            // Then
            assertThat(systemRole.isEditable()).isFalse();
            assertThat(systemRole.isDeletable()).isFalse();

            // Verificar que las operaciones fallan
            assertThatThrownBy(() -> systemRole.addPermission(permission1))
                    .isInstanceOf(BusinessRuleViolationException.class);

            assertThatThrownBy(() -> systemRole.delete())
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("INVARIANTE: Rol custom debe ser editable y deletable")
        void invariant_CustomRoleShouldBeEditableAndDeletable() {
            // Given
            Set<Permission> permissions = new HashSet<>();
            Rol customRole = editableRol.createCustom(RolEnum.RECEPTIONIST, "Custom", permissions);

            // Then
            assertThat(customRole.isDefault()).isFalse();
            assertThat(customRole.isEditable()).isTrue();
            assertThat(customRole.isDeletable()).isTrue();
        }

        @Test
        @DisplayName("INVARIANTE: Permisos deben ser inmutables desde fuera del agregado")
        void invariant_PermissionsShouldBeImmutableFromOutside() {
            // Given
            editableRol.addPermission(permission1);

            // When - Intentar modificar desde fuera
            Set<Permission> permissions = editableRol.getPermissions();
            permissions.clear(); // Modificar la copia retornada

            // Then - El agregado NO debe verse afectado (defensive copy)
            assertThat(editableRol.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("REGLA: Razón de cambio de estado debe tener mínimo 10 caracteres")
        void rule_StateChangeReasonMustBeAtLeast10Characters() {
            // Given
            String validReason = "0123456789"; // Exactamente 10

            // When & Then
            assertThatCode(() -> editableRol.activate(validReason))
                    .doesNotThrowAnyException();

            assertThatThrownBy(() -> editableRol.activate("012345678")) // 9 caracteres
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("REGLA: Solo roles deletables pueden ser marcados como DELETED")
        void rule_OnlyDeletableRolesCanBeMarkedDeleted() {
            // Given
            Rol nonDeletable = new Rol(RolEnum.PATIENT, "Patient", true, false, false, RolStatus.ACTIVE);
            Rol deletable = new Rol(RolEnum.GUARDIAN, "Guardian", false, true, true, RolStatus.ACTIVE);
            String reason = "Valid deletion reason";

            // When & Then
            assertThatThrownBy(() -> nonDeletable.markDeleted(reason))
                    .isInstanceOf(BusinessRuleViolationException.class);

            assertThatCode(() -> deletable.markDeleted(reason))
                    .doesNotThrowAnyException();
        }
    }

    // ===================================================================================
    // NESTED: Getters y Setters
    // ===================================================================================
    @Nested
    @DisplayName("Getters y Setters")
    class GettersAndSetters {

        @Test
        @DisplayName("Todos los getters deben retornar valores correctos")
        void allGetters_ShouldReturnCorrectValues() {
            // Given
            Rol rol = new Rol(
                    RolEnum.RECEPTIONIST,
                    "Test Role",
                    true,
                    false,
                    false,
                    RolStatus.ACTIVE
            );

            // When & Then
            assertThat(rol.getRolEnum()).isEqualTo(RolEnum.RECEPTIONIST);
            assertThat(rol.getDescription()).isEqualTo("Test Role");
            assertThat(rol.isDefault()).isTrue();
            assertThat(rol.isEditable()).isFalse();
            assertThat(rol.isDeletable()).isFalse();
            assertThat(rol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
            assertThat(rol.getPermissions()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Setters deben modificar valores correctamente")
        void setters_ShouldModifyValuesCorrectly() {
            // Given
            Rol rol = new Rol(RolEnum.PATIENT, "Patient", false, true, true, RolStatus.ACTIVE);

            // When
            rol.setDescription("New Description");
            rol.setDefault(true);
            rol.setEditable(false);
            rol.setDeletable(false);
            rol.setRolEnum(RolEnum.GUARDIAN);
            rol.setStatusRol(RolStatus.INACTIVE);

            // Then
            assertThat(rol.getDescription()).isEqualTo("New Description");
            assertThat(rol.isDefault()).isTrue();
            assertThat(rol.isEditable()).isFalse();
            assertThat(rol.isDeletable()).isFalse();
            assertThat(rol.getRolEnum()).isEqualTo(RolEnum.GUARDIAN);
            assertThat(rol.getStatusRol()).isEqualTo(RolStatus.INACTIVE);
        }

        @Test
        @DisplayName("setEditable(false) debe prevenir modificaciones posteriores")
        void setEditable_ToFalse_ShouldPreventFurtherModifications() {
            // Given
            editableRol.addPermission(permission1);

            // When
            editableRol.setEditable(false);

            // Then
            assertThatThrownBy(() -> editableRol.addPermission(permission2))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("setDeletable(false) debe prevenir eliminación")
        void setDeletable_ToFalse_ShouldPreventDeletion() {
            // Given & When
            editableRol.setDeletable(false);

            // Then
            assertThatThrownBy(() -> editableRol.delete())
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }

    // ===================================================================================
    // NESTED: Edge Cases
    // ===================================================================================
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Rol con descripción vacía debe ser permitido")
        void roleWithEmptyDescription_ShouldBeAllowed() {
            // When
            Rol rol = new Rol(RolEnum.PATIENT, "", false, true, true, RolStatus.ACTIVE);

            // Then
            assertThat(rol.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("Agregar y remover el mismo permiso múltiples veces")
        void addAndRemoveSamePermissionMultipleTimes_ShouldWork() {
            // When & Then
            editableRol.addPermission(permission1);
            assertThat(editableRol.hasPermission(permission1)).isTrue();

            editableRol.removePermission(permission1);
            assertThat(editableRol.hasPermission(permission1)).isFalse();

            editableRol.addPermission(permission1);
            assertThat(editableRol.hasPermission(permission1)).isTrue();

            editableRol.removePermission(permission1);
            assertThat(editableRol.hasPermission(permission1)).isFalse();
        }

        @Test
        @DisplayName("setPermissions con set vacío debe limpiar permisos")
        void setPermissions_WithEmptySet_ShouldClearPermissions() {
            // Given
            editableRol.addPermission(permission1);
            editableRol.addPermission(permission2);

            // When
            editableRol.setPermissions(new HashSet<>());

            // Then
            assertThat(editableRol.getPermissions()).isEmpty();
        }

        @Test
        @DisplayName("Cambios de estado rápidos consecutivos")
        void rapidConsecutiveStateChanges_ShouldWork() {
            // Given
            String reason = "Valid reason for testing";

            // When & Then - No debe haber problemas
            assertThatCode(() -> {
                editableRol.activate(reason);
                editableRol.suspend(reason);
                editableRol.deactivate(reason);
                editableRol.activate(reason);
                editableRol.suspend(reason);
            }).doesNotThrowAnyException();

            assertThat(editableRol.getStatusRol()).isEqualTo(RolStatus.SUSPENDED);
        }

        @Test
        @DisplayName("Razón con exactamente 10 caracteres debe ser válida")
        void reasonWithExactly10Characters_ShouldBeValid() {
            // Given
            String exactReason = "1234567890"; // 10 caracteres

            // When & Then
            assertThatCode(() -> editableRol.activate(exactReason))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Razón con espacios al inicio y final debe contar sin ellos")
        void reasonWithLeadingTrailingSpaces_ShouldCountWithoutThem() {
            // Given
            String reasonWithSpaces = "   short   "; // "short" = 5 caracteres después de trim

            // When & Then
            assertThatThrownBy(() -> editableRol.activate(reasonWithSpaces))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }
    }
}
