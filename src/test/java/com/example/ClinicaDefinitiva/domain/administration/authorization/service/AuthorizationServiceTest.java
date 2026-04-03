package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private Rol adminRol;
    private Rol patientRol;
    private RolId adminRolId;
    private RolId patientRolId;

    @BeforeEach
    void setUp() {
        adminRol = Rol.createCustom(RolEnum.ADMINISTRATOR, "Admin");
        patientRol = Rol.createCustom(RolEnum.PATIENT, "Paciente");
        adminRolId = RolId.of(1L);
        patientRolId = RolId.of(2L);
        adminRol.setId(adminRolId);
        patientRol.setId(patientRolId);
    }

    @Test
    @DisplayName("isAllowedByRole: Administrador tiene permiso")
    void isAllowedByRole_admin_allowed() {
        Permission permission = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE),ActionCatalog.of(ActionCatalog.BasicAction.READ));
        SecurityContext context = SecurityContext.builder(permission, UserIdentityId.from(1L)).build();

        assertThat(authorizationService.isAllowedByRole(adminRol, context)).isTrue();
    }

    @Test
    @DisplayName("isAllowedByRole: Paciente no tiene permiso de administrador")
    void isAllowedByRole_patient_notAllowed() {
        Permission permission = Permission.of(
                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                ActionCatalog.of(ActionCatalog.BasicAction.CREATE_CUSTOM)
        );
        SecurityContext context = SecurityContext.builder(permission, UserIdentityId.from(1L)).build();

        assertThat(authorizationService.isAllowedByRole(patientRol, context)).isFalse();
    }

    @Test
    @DisplayName("isAuthorizedByContext: Verifica políticas ABAC")
    void isAuthorizedByContext_checksPolicies() {
        when(rolRepository.findById(patientRolId)).thenReturn(Optional.of(patientRol));

        Permission permission = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT),ActionCatalog.of(ActionCatalog.BasicAction.READ));
        SecurityContext context = SecurityContext.builder(permission, UserIdentityId.from(1L))
                .withResourceOwnerId(UserIdentityId.from(1L))
                .build();

        // Paciente viendo sus propios datos → OwnershipPolicy permite
        assertThat(authorizationService.isAuthorizedByContext(patientRolId, context)).isTrue();
    }

    @Test
    @DisplayName("isAuthorizedByContext: Deniega cuando OwnershipPolicy falla")
    void isAuthorizedByContext_denies() {
        when(rolRepository.findById(patientRolId)).thenReturn(Optional.of(patientRol));

        Permission permission = Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT),ActionCatalog.of(ActionCatalog.BasicAction.READ));
        SecurityContext context = SecurityContext.builder(permission, UserIdentityId.from(1L))
                .withResourceOwnerId(UserIdentityId.from(2L)) // Dueño diferente
                .build();

        assertThat(authorizationService.isAuthorizedByContext(patientRolId, context)).isFalse();
    }

    @Test
    @DisplayName("hasPermission: Método de conveniencia")
    void hasPermission() {
        when(rolRepository.findById(adminRolId)).thenReturn(Optional.of(adminRol));

        boolean result = authorizationService.hasPermission(
                adminRolId,
                UserIdentityId.from(1L),
                ResourceCatalog.of(ResourceCatalog.BasicResource.ROLE),
                ActionCatalog.of(ActionCatalog.BasicAction.CREATE_CUSTOM)
        );

        assertThat(result).isTrue();
    }
}