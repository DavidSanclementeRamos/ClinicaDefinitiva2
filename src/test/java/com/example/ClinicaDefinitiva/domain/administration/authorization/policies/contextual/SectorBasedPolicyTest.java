package com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SectorBasedPolicyTest {

    private SectorBasedPolicy policy;
    private Rol receptionistRol;
    private UserIdentityId requesterId;

    @BeforeEach
    void setUp() {
        policy = new SectorBasedPolicy();
        receptionistRol = Rol.createCustom(RolEnum.RECEPTIONIST, "Recepcionista");
        requesterId = UserIdentityId.from(1L);
    }

    @Test
    @DisplayName("Sector: Recepcionista de RRHH puede eliminar dentista")
    void sectorRRHH_allowed() {
        Permission permission = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST),ActionCatalog.of(ActionCatalog.BasicAction.DELETE));
        SecurityContext context = SecurityContext.builder(permission, requesterId)
                .withSector("RECURSOS_HUMANOS")
                .build();

        assertThat(policy.isAllowed(receptionistRol, context)).isTrue();
    }

    @Test
    @DisplayName("Sector: Recepcionista de otro sector no puede eliminar dentista")
    void sectorOther_denied() {
        Permission permission = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST),ActionCatalog.of(ActionCatalog.BasicAction.DELETE));
        SecurityContext context = SecurityContext.builder(permission, requesterId)
                .withSector("FRONT_OFFICE")
                .build();

        assertThat(policy.isAllowed(receptionistRol, context)).isFalse();
    }

    @Test
    @DisplayName("Sector: Política no aplica para otros recursos")
    void doesNotApplyToOtherResources() {
        Permission permission = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT),ActionCatalog.of(ActionCatalog.BasicAction.READ));
        SecurityContext context = SecurityContext.builder(permission, requesterId).build();

        assertThat(policy.appliesTo(context)).isFalse();
    }
}