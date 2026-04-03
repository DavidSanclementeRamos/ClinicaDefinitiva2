package com.example.ClinicaDefinitiva.domain.administration.authorization.policies.contextual;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OwnershipPolicyTest {

    private OwnershipPolicy policy;
    private Rol patientRol;
    private Rol guardianRol;
    private UserIdentityId requesterId;
    private UserIdentityId resourceOwnerId;

    @BeforeEach
    void setUp() {
        policy = new OwnershipPolicy();
        patientRol = Rol.createCustom(RolEnum.PATIENT, "Paciente");
        guardianRol = Rol.createCustom(RolEnum.GUARDIAN, "Tutor");
        requesterId = UserIdentityId.from(1L);
        resourceOwnerId = UserIdentityId.from(1L);
    }

    @Test
    @DisplayName("Ownership: Paciente puede ver sus propios datos")
    void patientOwnership_allowed() {
        Permission permission = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        SecurityContext context = SecurityContext.builder(permission, requesterId)
                .withResourceOwnerId(resourceOwnerId)
                .build();

        assertThat(policy.isAllowed(patientRol, context)).isTrue();
    }

    @Test
    @DisplayName("Ownership: Paciente no puede ver datos de otro paciente")
    void patientOwnership_denied() {
        Permission permission = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        SecurityContext context = SecurityContext.builder(permission, requesterId)
                .withResourceOwnerId(UserIdentityId.from(2L))
                .build();

        assertThat(policy.isAllowed(patientRol, context)).isFalse();
    }

    @Test
    @DisplayName("Guardianship: Tutor puede ver datos de paciente bajo tutela")
    void guardianship_allowed() {
        Permission permission = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        SecurityContext context = SecurityContext.builder(permission, requesterId)
                .withPatientGuardianId(requesterId.value())
                .build();

        assertThat(policy.isAllowed(guardianRol, context)).isTrue();
    }

    @Test
    @DisplayName("Guardianship: Tutor no puede ver datos de paciente sin tutela")
    void guardianship_denied() {
        Permission permission = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        SecurityContext context = SecurityContext.builder(permission, requesterId)
                .withPatientGuardianId(2L)
                .build();

        assertThat(policy.isAllowed(guardianRol, context)).isFalse();
    }
}
