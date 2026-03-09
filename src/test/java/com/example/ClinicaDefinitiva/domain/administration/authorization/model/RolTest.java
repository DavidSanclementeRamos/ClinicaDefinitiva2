
package com.example.ClinicaDefinitiva.domain.administration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization.RolError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;


@DisplayName("Tests del Agregado Rol")
class RolTest {

    @Nested
    @DisplayName("Tests de creación de Rol")
    class RolCreationTests {

        @Test
        @DisplayName("Debe crear un rol por defecto exitosamente")
        void shouldCreateDefaultRoleSuccessfully() {
            // Act
            Rol rol = Rol.createDefault(RolEnum.ADMINISTRATOR, "Administrador del sistema");

            // Assert
            assertNotNull(rol);
            assertEquals(RolEnum.ADMINISTRATOR, rol.getRolEnum());
            assertEquals("Administrador del sistema", rol.getDescription());
            assertTrue(rol.isDefault());
            assertFalse(rol.isEditable());
            assertFalse(rol.isDeletable());
            assertEquals(RolStatus.ACTIVE, rol.getStatusRol());
            assertTrue(rol.getPermissions().isEmpty());
        }

        @Test
        @DisplayName("Debe clonar un rol existente exitosamente")
        void shouldCloneExistingRoleSuccessfully() {
            // Arrange
            Rol sourceRole = Rol.createDefault(RolEnum.DENTIST, "Odontólogo base");

            // Act
            Rol clonedRole = Rol.cloneFrom(sourceRole, "Odontólogo especialista");

            // Assert
            assertNotNull(clonedRole);
            assertEquals(sourceRole.getRolEnum(), clonedRole.getRolEnum());
            assertEquals("Odontólogo especialista", clonedRole.getDescription());
            assertFalse(clonedRole.isDefault());
            assertTrue(clonedRole.isEditable());
            assertTrue(clonedRole.isDeletable());
            assertEquals(RolStatus.ACTIVE, clonedRole.getStatusRol());
        }
    }

    @Nested
    @DisplayName("Tests de gestión de permisos")
    class PermissionManagementTests {

        private Rol rol;
        private Permission permission1;
        private Permission permission2;

        @BeforeEach
        void setUp() {
            rol = Rol.cloneFrom(
                Rol.createDefault(RolEnum.DENTIST, "Odontólogo base"),
                "Odontólogo con permisos personalizados"
            );
            permission1 = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT));
            permission2 = Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT));
        }

        @Test
        @DisplayName("Debe agregar un permiso correctamente")
        void shouldAddPermissionSuccessfully() {
            // Act
            rol.addPermission(permission1);

            // Assert
            assertTrue(rol.getPermissions().contains(permission1));
            assertEquals(1, rol.getPermissions().size());
        }

        @Test
        @DisplayName("Debe remover un permiso correctamente")
        void shouldRemovePermissionSuccessfully() {
            // Arrange
            rol.addPermission(permission1);
            rol.addPermission(permission2);

            // Act
            rol.removePermission(permission1);

            // Assert
            assertFalse(rol.getPermissions().contains(permission1));
            assertTrue(rol.getPermissions().contains(permission2));
            assertEquals(1, rol.getPermissions().size());
        }

        @Test
        @DisplayName("Debe verificar si tiene un permiso específico")
        void shouldCheckIfHasPermission() {
            // Arrange
            rol.addPermission(permission1);

            // Act & Assert
            assertTrue(rol.hasPermission(permission1));
            assertFalse(rol.hasPermission(permission2));
        }

        @Test
        @DisplayName("Debe lanzar excepción al agregar permiso a rol no editable")
        void shouldThrowExceptionWhenAddingPermissionToNonEditableRole() {
            // Arrange
            Rol defaultRol = Rol.createDefault(RolEnum.ADMINISTRATOR, "Admin");

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> defaultRol.addPermission(permission1)
            );
            
            assertEquals(RolError.ERR_ROL_SYSTEM_NOT_EDITABLE, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe establecer conjunto de permisos completo")
        void shouldSetPermissionsSet() {
            // Arrange
            Set<Permission> permissions = Set.of(permission1, permission2);

            // Act
            rol.setPermissions(permissions);

            // Assert
            assertEquals(2, rol.getPermissions().size());
            assertTrue(rol.getPermissions().contains(permission1));
            assertTrue(rol.getPermissions().contains(permission2));
        }
    }

    @Nested
    @DisplayName("Tests de gestión de estado")
    class RolStatusTests {

        private Rol rol;

        @BeforeEach
        void setUp() {
            rol = Rol.cloneFrom(
                Rol.createDefault(RolEnum.DENTIST, "Odontólogo base"),
                "Rol editable"
            );
        }

        @Test
        @DisplayName("Debe activar el rol correctamente")
        void shouldActivateRole() {
            // Arrange
            rol.deactivate("Razón de desactivación");

            // Act
            rol.activate("Razón de activación");

            // Assert
            assertEquals(RolStatus.ACTIVE, rol.getStatusRol());
        }

        @Test
        @DisplayName("Debe desactivar el rol correctamente")
        void shouldDeactivateRole() {
            // Act
            rol.deactivate("Razón de desactivación válida con más de 10 caracteres");

            // Assert
            assertEquals(RolStatus.INACTIVE, rol.getStatusRol());
        }

        @Test
        @DisplayName("Debe suspender el rol correctamente")
        void shouldSuspendRole() {
            // Act
            rol.suspend("Razón de suspensión válida con más de 10 caracteres");

            // Assert
            assertEquals(RolStatus.SUSPENDED, rol.getStatusRol());
        }

        @Test
        @DisplayName("Debe marcar como eliminado correctamente")
        void shouldMarkAsDeleted() {
            // Act
            rol.markDeleted("Razón de eliminación válida con más de 10 caracteres");

            // Assert
            assertEquals(RolStatus.DELETED, rol.getStatusRol());
        }

        @Test
        @DisplayName("Debe lanzar excepción al cambiar estado sin razón válida")
        void shouldThrowExceptionWhenChangingStateWithoutValidReason() {
            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> rol.deactivate("Corta")
            );
            
            assertEquals(RolError.ERR_ROL_DELETE_REASON_REQUIRED, exception.getCatalogo());
        }
    }
}