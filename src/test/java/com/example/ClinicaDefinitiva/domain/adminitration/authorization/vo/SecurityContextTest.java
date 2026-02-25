
package com.example.ClinicaDefinitiva.domain.adminitration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SecurityContextTest {

    @Test
    void shouldCreateSecurityContextWithPermissionAndUser() {
        Permission permission = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY));
        UserIdentityId userId = UserIdentityId.from(1L);

        SecurityContext context = SecurityContext.builder(permission, userId).build();

        assertEquals(permission, context.getPermission());
        assertEquals(userId, context.getRequestingUserId());
    }

    @Test
    void shouldStoreAndRetrieveCustomAttribute() {
        Permission permission = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        UserIdentityId userId = UserIdentityId.from(2L);

        SecurityContext context = SecurityContext.builder(permission, userId)
                .withAttribute("customKey", "customValue")
                .build();

        Optional<String> value = context.getAttribute("customKey", String.class);
        assertTrue(value.isPresent());
        assertEquals("customValue", value.get());
    }

    @Test
    void shouldReturnEmptyOptionalForWrongType() {
        Permission permission = Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT));
        UserIdentityId userId = UserIdentityId.from(3L);

        SecurityContext context = SecurityContext.builder(permission, userId)
                .withAttribute("resourceId", 123L)
                .build();

        Optional<String> wrongType = context.getAttribute("resourceId", String.class);
        assertTrue(wrongType.isEmpty());
    }

    @Test
    void shouldStoreAndRetrieveABACAttributes() {
        Permission permission = Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE));
        UserIdentityId userId = UserIdentityId.from(4L);

        SecurityContext context = SecurityContext.builder(permission, userId)
                .withResourceOwnerId(UserIdentityId.from(99L))
                .withSector("Odontología")
                .withDentistSpecialties(Set.of("Ortodoncia", "Endodoncia"))
                .build();

        assertEquals(UserIdentityId.from(99L),
                context.getAttribute("resourceOwnerId", UserIdentityId.class).orElse(null));
        assertEquals("Odontología",
                context.getAttribute("sector", String.class).orElse(null));
        assertTrue(context.getAttribute("dentistSpecialties", Set.class).isPresent());
    }
}

