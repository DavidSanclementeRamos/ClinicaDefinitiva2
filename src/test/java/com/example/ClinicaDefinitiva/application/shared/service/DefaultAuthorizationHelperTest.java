package com.example.ClinicaDefinitiva.application.shared.service;

import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.Sector.Type;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuditLogger;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeAll;
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
class DefaultAuthorizationHelperTest {

    @Mock
    private ReceptionRepository receptionRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private AuditLogger auditLogger;

    @InjectMocks
    private DefaultAuthorizationHelper helper;

    private static final UserIdentityId USER_ID = UserIdentityId.from(1L);
    private static final RolId ROL_ID = RolId.of(1L);
    private static final ResourceCatalog.BasicResource RESOURCE = ResourceCatalog.BasicResource.PATIENT;
    private static final ActionCatalog.BasicAction ACTION = ActionCatalog.BasicAction.READ;
    
    @BeforeEach
    public void setup() {
        Rol testRol = mock(Rol.class);
        lenient().when(testRol.getRolEnum()).thenReturn(RolEnum.RECEPTIONIST);
        lenient().when(rolRepository.findById(ROL_ID)).thenReturn(Optional.of(testRol));
    }

    @Test
    @DisplayName("Autorización exitosa sin sector validation")
    void authorize_success_withoutSector() {
        when(authorizationService.isAuthorizedByContext(any(), any())).thenReturn(true);

        assertThatCode(() -> helper.authorize(USER_ID, ROL_ID, RESOURCE, ACTION, AuthorizationContext.builder().build()))
                .doesNotThrowAnyException();

        verify(authorizationService).isAuthorizedByContext(eq(ROL_ID), any());
        verify(auditLogger).logAuthorizationDecision(any());
    }

    @Test
    @DisplayName("Autorización denegada lanza excepción")
    void authorize_denied_throws() {
        when(authorizationService.isAuthorizedByContext(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> helper.authorize(USER_ID, ROL_ID, RESOURCE, ACTION, AuthorizationContext.builder().build()))
                .isInstanceOf(BusinessRuleViolationException.class);

        verify(auditLogger).logAuthorizationDecision(any());
    }

    @Test
    @DisplayName("CheckAuthorization retorna false cuando denegado")
    void checkAuthorization_denied_returnsFalse() {
        when(authorizationService.isAuthorizedByContext(any(), any())).thenReturn(false);

        boolean result = helper.checkAuthorization(USER_ID, ROL_ID, RESOURCE, ACTION, AuthorizationContext.builder().build());

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("CheckAuthorization retorna true cuando permitido")
    void checkAuthorization_allowed_returnsTrue() {
        when(authorizationService.isAuthorizedByContext(any(), any())).thenReturn(true);

        boolean result = helper.checkAuthorization(USER_ID, ROL_ID, RESOURCE, ACTION, AuthorizationContext.builder().build());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Eliminar dentista requiere sector validation (RRHH)")
    void authorize_deleteDentist_requiresSector() {
        Receptionist receptionist = mock(Receptionist.class);
        when(receptionist.getSector()).thenReturn(com.example.ClinicaDefinitiva.domain.actor.vo.Sector.of(Type.HUMAN_RESOURCES));
        when(receptionRepository.findByUserId(USER_ID)).thenReturn(Optional.of(receptionist));
        when(authorizationService.isAuthorizedByContext(any(), any())).thenReturn(true);
        when(authorizationService.requiresSectorContext(any(), any())).thenReturn(true);

        assertThatCode(() -> helper.authorize(
                USER_ID, ROL_ID,
                ResourceCatalog.BasicResource.DENTIST,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder().build()
        )).doesNotThrowAnyException();

        verify(receptionRepository).findByUserId(USER_ID);
    }

    @Test
    @DisplayName("Eliminar dentista sin sector RRHH lanza excepción")
    void authorize_deleteDentist_wrongSector_throws() {
        lenient().when(receptionRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> helper.authorize(
                USER_ID, ROL_ID,
                ResourceCatalog.BasicResource.DENTIST,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder().build()
        )).isInstanceOf(BusinessRuleViolationException.class);
    }
}
