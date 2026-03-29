package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SecurityContextTest {

    @Test
    @DisplayName("Construir SecurityContext con atributos")
    void buildSecurityContext() {
        UserIdentityId userId = UserIdentityId.from(1L);
        Permission permission = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));

        SecurityContext context = SecurityContext.builder(permission, userId)
                .withResourceId(100L)
                .withResourceOwnerId(userId)
                .withSector("RECURSOS_HUMANOS")
                .build();

        assertThat(context.getPermission()).isEqualTo(permission);
        assertThat(context.getRequestingUserId()).isEqualTo(userId);
        assertThat(context.getAttribute("resourceId", Long.class)).contains(100L);
        assertThat(context.getAttribute("resourceOwnerId", UserIdentityId.class)).contains(userId);
        assertThat(context.getAttribute("sector", String.class)).contains("RECURSOS_HUMANOS");
    }
}
