package com.example.ClinicaDefinitiva.application.authorization.service;

import com.example.ClinicaDefinitiva.application.authentication.dto.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.mapper.UserIdentityReadMapper;
import com.example.ClinicaDefinitiva.application.authentication.mapper.UserIdentityWriteMapper;
import com.example.ClinicaDefinitiva.application.authentication.service.UserIdentityApplicationService;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.AggregateBusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdentityApplicationServiceTest {

    @Mock
    private UserIdentityRepository userIdentityRepository;
    @Mock
    private UserDeactivationPolicy userDeactivationPolicy;
    @Mock
    private UserIdentityReadMapper readMapper;
    @Mock
    private UserIdentityWriteMapper writeMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthorizationHelper authorizationHelper;
    @Mock
    private UserAccessValidator userAccessValidator;

    @InjectMocks
    private UserIdentityApplicationService service;

    private final UserIdentityId requesterId = UserIdentityId.from(1L);
    private final RolId requesterRolId = RolId.of(100L);
    private final UserIdentityId targetUserId = UserIdentityId.from(2L);

    private UserIdentity createUser() {
        Email email = Email.ofOrThrow("test@example.com");
        HashedPassword password = HashedPassword.of("encodedPassword");
        UserIdentityName name = UserIdentityName.of("testuser");
        return UserIdentity.register(email, password, name, Instant.now());
    }

    private UserIdentity createVerifiedUser() {
        UserIdentity user = createUser();
        user.verify();
        return user;
    }

    private ReadUserIdentityDto createReadDto() {
        return new ReadUserIdentityDto(
            2L,
            "test@example.com",
            "testuser",
            Instant.now(),
            null,
            0,
            null,
            true,
            "ACTIVE"
            
        );
    }

    // ========== findById ==========
    @Test
    @DisplayName("findById - debe retornar DTO cuando existe")
    void findById_shouldReturnDto() {
        UserIdentity user = createUser();
        ReadUserIdentityDto expectedDto = createReadDto();

        when(userIdentityRepository.findById(targetUserId)).thenReturn(Optional.of(user));
        when(readMapper.toReadDto(user)).thenReturn(expectedDto);

        ReadUserIdentityDto result = service.findById(targetUserId, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.USER_IDENTITY), eq(ActionCatalog.BasicAction.READ),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("findById - lanza excepción cuando no existe")
    void findById_shouldThrowWhenNotFound() {
        when(userIdentityRepository.findById(targetUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(targetUserId, requesterId, requesterRolId))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    // ========== findAll ==========
    @Test
    @DisplayName("findAll - debe retornar página")
    void findAll_shouldReturnPage() {
        Pageable pageable = Pageable.unpaged();
        UserIdentity user = createUser();
        Page<UserIdentity> page = new PageImpl<>(List.of(user));
        PageUserIdentityDto dto = new PageUserIdentityDto(1L, "test@example.com", "testuser",true, "ACTIVE");

        when(userIdentityRepository.findAll(pageable)).thenReturn(page);
        when(readMapper.toPageDto(user)).thenReturn(dto);

        Page<PageUserIdentityDto> result = service.findAll(pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    // ========== register ==========
    @Test
    @DisplayName("register - debe registrar usuario")
    void register_shouldRegister() {
        CreateUserIdentityDto dto = new CreateUserIdentityDto("test@example.com", "password", "testuser");
        Email email = Email.ofOrThrow("test@example.com");
        HashedPassword hashedPassword = HashedPassword.of("encodedPassword");
        UserIdentityName name = UserIdentityName.of("testuser");
        UserIdentity user = UserIdentity.register(email, hashedPassword, name, Instant.now());
        ReadUserIdentityDto expectedDto = createReadDto();

        when(writeMapper.toEmail(dto)).thenReturn(email);
        when(writeMapper.toPassword(dto)).thenReturn(hashedPassword);
        when(writeMapper.toUserName(dto)).thenReturn(name);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userIdentityRepository.save(any(UserIdentity.class))).thenReturn(user);
        when(readMapper.toReadDto(user)).thenReturn(expectedDto);
        when(userIdentityRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        ReadUserIdentityDto result = service.register(dto, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(userIdentityRepository).save(any(UserIdentity.class));
    }

    // ========== update ==========
    @Test
@DisplayName("update - debe actualizar usuario")
void update_shouldUpdate() {
    Long id = 2L;
    // ✅ El DTO tiene la contraseña correcta
    UpdateUserIdentityDto dto = new UpdateUserIdentityDto("new@example.com", "newname","newpassword" );
    UserIdentity user = createVerifiedUser();
    ReadUserIdentityDto expectedDto = createReadDto();

    when(userIdentityRepository.findById(targetUserId)).thenReturn(Optional.of(user));
    when(writeMapper.toUserName(dto)).thenReturn(Optional.of(UserIdentityName.of("newname")));
    when(writeMapper.toEmail(dto)).thenReturn(Optional.of(Email.ofOrThrow("new@example.com")));
    when(writeMapper.toPassword(dto)).thenReturn(Optional.of(HashedPassword.of("newpassword")));
    
    // ✅ Stub correcto: codificar la contraseña, no el nombre
    when(passwordEncoder.encode("newpassword")).thenReturn("newEncodedPassword");
    
    when(userIdentityRepository.save(user)).thenReturn(user);
    when(readMapper.toReadDto(user)).thenReturn(expectedDto);
    when(userIdentityRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

    ReadUserIdentityDto result = service.update(dto, id, requesterId, requesterRolId);

    assertThat(result).isEqualTo(expectedDto);
    verify(userIdentityRepository).save(user);
}

    // ========== deactivate ==========
    @Test
    @DisplayName("deactivate - debe desactivar usuario")
    void deactivate_shouldDeactivate() {
        UserIdentity user = createVerifiedUser();
        when(userIdentityRepository.findById(targetUserId)).thenReturn(Optional.of(user));
        when(userDeactivationPolicy.validate(user)).thenReturn(Outcome.ok());
        when(userIdentityRepository.save(user)).thenReturn(user);
        ReadUserIdentityDto expectedDto = createReadDto();
        when(readMapper.toReadDto(user)).thenReturn(expectedDto);

        ReadUserIdentityDto result = service.deactivate(targetUserId, "Razón", requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.INACTIVE);
        verify(userIdentityRepository).save(user);
    }

    // ========== suspend ==========
    @Test
    @DisplayName("suspend - debe suspender usuario")
    void suspend_shouldSuspend() {
        UserIdentity user = createVerifiedUser();
        when(userIdentityRepository.findById(targetUserId)).thenReturn(Optional.of(user));
        when(userIdentityRepository.save(user)).thenReturn(user); // ✅ IMPORTANTE: stub del save
        ReadUserIdentityDto expectedDto = createReadDto();
        when(readMapper.toReadDto(user)).thenReturn(expectedDto);

        ReadUserIdentityDto result = service.suspend(targetUserId, "Motivo", requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.SUSPENDED);
        verify(userIdentityRepository).save(user);
    }

    // ========== authenticate ==========
    @Test
    @DisplayName("authenticate - debe autenticar con credenciales correctas")
    void authenticate_shouldSuccess() {
        String email = "test@example.com";
        String rawPassword = "password";
        UserIdentity user = createVerifiedUser();
        String encodedPassword = "encodedPassword";
        HashedPassword hashedPassword = HashedPassword.of(encodedPassword);
        
        // ✅ CORREGIDO: usar matchers para todos los argumentos
        doNothing().when(authorizationHelper).authorize(
                eq(requesterId), 
                eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.USER_IDENTITY),
                eq(ActionCatalog.BasicAction.AUTHENTICATE),
                any(AuthorizationContext.class)
        );
        
        when(userIdentityRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        doNothing().when(userAccessValidator).validateUserCanPerformSensitiveAction(
                eq(user.getId()),
                any(Instant.class),
                eq(EntityContext.USER_IDENTITY)
        );
        when(userIdentityRepository.save(user)).thenReturn(user);
        ReadUserIdentityDto expectedDto = createReadDto();
        when(readMapper.toReadDto(user)).thenReturn(expectedDto);

        ReadUserIdentityDto result = service.authenticate(email, rawPassword, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(userIdentityRepository).save(user);
    }

    @Test
    @DisplayName("authenticate - debe fallar con contraseña incorrecta")
    void authenticate_shouldFailWrongPassword() {
        String email = "test@example.com";
        String rawPassword = "wrong";
        UserIdentity user = createVerifiedUser();
        String encodedPassword = "encodedPassword";

        when(userIdentityRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        when(userIdentityRepository.save(user)).thenReturn(user);

        assertThatThrownBy(() -> service.authenticate(email, rawPassword, requesterId, requesterRolId))
                .isInstanceOf(AggregateBusinessRuleViolationException.class);

        // ✅ CORREGIDO: verificar sobre el mock, no sobre el objeto real
        verify(userIdentityRepository).save(user);
        verify(userAccessValidator, never()).validateUserCanPerformSensitiveAction(any(), any(), any());
    }

    // ========== reactivate ==========
    @Test
    @DisplayName("reactivate - debe reactivar usuario")
    void reactivate_shouldReactivate() {
        UserIdentity user = createVerifiedUser();
        // Desactivar primero
        when(userDeactivationPolicy.validate(user)).thenReturn(Outcome.ok());
        user.deactivate(userDeactivationPolicy, Instant.now(), "Razón");
        
        when(userIdentityRepository.findById(targetUserId)).thenReturn(Optional.of(user));
        when(userIdentityRepository.save(user)).thenReturn(user);
        ReadUserIdentityDto expectedDto = createReadDto();
        when(readMapper.toReadDto(user)).thenReturn(expectedDto);

        ReadUserIdentityDto result = service.reactivate(targetUserId, "Razón", requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.ACTIVE);
        verify(userIdentityRepository).save(user);
    }
}