
package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PermissionTest {

    @Test
    void shouldCreatePermissionWithCreateAction() {
        ResourceCatalog resource = ResourceCatalog.custom("USER");
        Permission permission = Permission.create(resource);

        assertEquals("CREATE_USER", permission.getCode());
        assertEquals(ActionCatalog.BasicAction.CREATE.name(), permission.getAction().getCode());
    }

    @Test
    void shouldCreatePermissionWithReadAction() {
        ResourceCatalog resource = ResourceCatalog.custom("USER");
        Permission permission = Permission.read(resource);

        assertEquals("READ_USER", permission.getCode());
    }

    @Test
    void shouldCreatePermissionWithCustomAction() {
        ResourceCatalog resource = ResourceCatalog.custom("REPORT");
        ActionCatalog action = ActionCatalog.custom("EXPORT");
        Permission permission = Permission.of(resource, action);

        assertEquals("EXPORT_REPORT", permission.getCode());
    }

    @Test
    void shouldBeEqualWhenResourceAndActionMatch() {
        ResourceCatalog resource = ResourceCatalog.custom("USER");
        Permission p1 = Permission.update(resource);
        Permission p2 = Permission.of(resource, ActionCatalog.of(ActionCatalog.BasicAction.UPDATE));

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenResourceDiffers() {
        Permission p1 = Permission.delete(ResourceCatalog.custom("USER"));
        Permission p2 = Permission.delete(ResourceCatalog.custom("REPORT"));

        assertNotEquals(p1, p2);
    }

    @Test
    void shouldNotBeEqualWhenActionDiffers() {
        ResourceCatalog resource = ResourceCatalog.custom("USER");
        Permission p1 = Permission.create(resource);
        Permission p2 = Permission.read(resource);

        assertNotEquals(p1, p2);
    }
}

