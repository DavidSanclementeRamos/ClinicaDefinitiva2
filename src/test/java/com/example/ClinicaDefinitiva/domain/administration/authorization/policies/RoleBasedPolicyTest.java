package com.example.ClinicaDefinitiva.domain.administration.authorization.policies;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RoleBasedPolicyTest {

    private RoleBasedPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new RoleBasedPolicy();
    }

    @Test
    @DisplayName("Administrator puede gestionar roles")
    void administratorCanManageRoles() {
        Rol adminRol = Rol.createCustom(RolEnum.ADMINISTRATOR, "Admin");
        Permission perm = Permission.of(
                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                ActionCatalog.of(ActionCatalog.BasicAction.CREATE_CUSTOM)
        );
        SecurityContext context = SecurityContext.builder(perm, UserIdentityId.from(1L)).build();

        assertThat(policy.isAllowed(adminRol, context)).isTrue();
    }

    @Test
    @DisplayName("Patient no puede gestionar roles")
    void patientCannotManageRoles() {
        Rol patientRol = Rol.createCustom(RolEnum.PATIENT, "Paciente");
        Permission perm = Permission.of(
                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                ActionCatalog.of(ActionCatalog.BasicAction.CREATE_CUSTOM)
        );
        SecurityContext context = SecurityContext.builder(perm, UserIdentityId.from(1L)).build();

        assertThat(policy.isAllowed(patientRol, context)).isFalse();
    }

    @Test
    @DisplayName("Receptionist puede gestionar pacientes")
    void receptionistCanManagePatients() {
        Rol receptionistRol = Rol.createCustom(RolEnum.RECEPTIONIST, "Recepcionista");
        Permission perm = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT),ActionCatalog.of(ActionCatalog.BasicAction.CREATE));
        SecurityContext context = SecurityContext.builder(perm, UserIdentityId.from(1L)).build();

        assertThat(policy.isAllowed(receptionistRol, context)).isTrue();
    }

    @Test
    @DisplayName("Dentist puede leer sus servicios")
    void dentistCanReadServices() {
        Rol dentistRol = Rol.createCustom(RolEnum.DENTIST, "Dentista");
        Permission perm = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE),ActionCatalog.of(ActionCatalog.BasicAction.READ));
        SecurityContext context = SecurityContext.builder(perm, UserIdentityId.from(1L)).build();

        assertThat(policy.isAllowed(dentistRol, context)).isTrue();
    }

    @Test
    @DisplayName("Patient puede leer sus facturas")
    void patientCanReadInvoices() {
        Rol patientRol = Rol.createCustom(RolEnum.PATIENT, "Paciente");
        Permission perm = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE),ActionCatalog.of(ActionCatalog.BasicAction.READ));
        SecurityContext context = SecurityContext.builder(perm, UserIdentityId.from(1L)).build();

        assertThat(policy.isAllowed(patientRol, context)).isTrue();
    }
}
