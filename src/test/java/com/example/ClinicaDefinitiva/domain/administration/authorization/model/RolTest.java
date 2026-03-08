
package com.example.ClinicaDefinitiva.domain.administration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

class RolTest {

    @Test
    void shouldAddAndRemovePermissionWhenEditable() {
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Administrador", false, true, true, RolStatus.ACTIVE);
        Permission p = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY));

        rol.addPermission(p);
        assertTrue(rol.hasPermission(p));

        rol.removePermission(p);
        assertFalse(rol.hasPermission(p));
    }

    @Test
    void shouldThrowWhenAddingPermissionIfNotEditable() {
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Administrador", false, false, true, RolStatus.ACTIVE);
        Permission p = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY));

        assertThrows(BusinessRuleViolationException.class, () -> rol.addPermission(p));
    }

    @Test
    void shouldThrowWhenDeletingIfNotDeletable() {
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Administrador", false, true, false, RolStatus.ACTIVE);
        assertThrows(BusinessRuleViolationException.class, rol::delete);
    }

    @Test
    void shouldChangeStatusWithValidReason() {
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Administrador", false, true, true, RolStatus.INACTIVE);

        rol.activate("Cambio de estado por requerimiento válido");
        assertEquals(RolStatus.ACTIVE, rol.getStatusRol());

        rol.suspend("Suspensión temporal por auditoría");
        assertEquals(RolStatus.SUSPENDED, rol.getStatusRol());
    }

    @Test
    void shouldThrowWhenReasonIsTooShort() {
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Administrador", false, true, true, RolStatus.ACTIVE);
        assertThrows(BusinessRuleViolationException.class, () -> rol.deactivate("corto"));
    }

    @Test
    void shouldMarkDeletedWhenDeletableAndReasonValid() {
        Rol rol = new Rol(RolEnum.ADMINISTRATOR, "Administrador", false, true, true, RolStatus.ACTIVE);
        rol.markDeleted("Eliminación solicitada por migración de datos");
        assertEquals(RolStatus.DELETED, rol.getStatusRol());
    }
}

