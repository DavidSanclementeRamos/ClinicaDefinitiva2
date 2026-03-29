
package com.example.ClinicaDefinitiva.domain.authentication.model;

import com.example.ClinicaDefinitiva.domain.authentication.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.vo.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdentityTest {

    @Mock
    private UserDeactivationPolicy deactivationPolicy;

    private Email email;
    private HashedPassword hashedPassword;
    private UserIdentityName name;
    private Instant now;

    @BeforeEach
    void setUp() {
        email = Email.ofOrThrow("test@example.com");
        hashedPassword = HashedPassword.of("hashedPassword");
        name = UserIdentityName.of("testuser");
        now = Instant.now();
    }

    private UserIdentity createUser() {
        return UserIdentity.register(email, hashedPassword, name, now);
    }

    // ========== REGISTRO ==========
    @Test
    @DisplayName("Registrar nuevo usuario")
    void shouldRegister() {
        UserIdentity user = createUser();

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getHashedPassword()).isEqualTo(hashedPassword);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.isVerified()).isFalse();
        // ✅ CORREGIDO: comparar el valor interno
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.ACTIVE);
        assertThat(user.getFailedLoginAttempts()).isZero();
        assertThat(user.getLockedUntil()).isNull();
        assertThat(user.getLastLoginAt()).isNull();
    }

    // ========== LOGIN EXITOSO ==========
    @Test
    @DisplayName("Registrar login exitoso")
    void shouldRecordSuccessfulLogin() {
        UserIdentity user = createUser();
        Instant loginTime = now.plusSeconds(10);

        Outcome<UserIdentity> outcome = user.recordSuccessfulLogin(loginTime);

        assertThat(outcome.isSuccess()).isTrue();
        assertThat(user.getLastLoginAt()).isEqualTo(loginTime);
        assertThat(user.getFailedLoginAttempts()).isZero();
    }

    @Test
    @DisplayName("Registrar login exitoso cuando usuario está bloqueado falla")
    void shouldFailLoginWhenLocked() {
        UserIdentity user = createUser();
        int maxAttempts = 3;
        Duration lockDuration = Duration.ofMinutes(5);

        // Tres intentos fallidos para bloquear
        user.recordFailedLogin(now, maxAttempts, lockDuration);
        user.recordFailedLogin(now, maxAttempts, lockDuration);
        user.recordFailedLogin(now, maxAttempts, lockDuration);

        Instant loginTime = now.plusSeconds(1);
        Outcome<UserIdentity> outcome = user.recordSuccessfulLogin(loginTime);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_FAILED_ATTEMPTS_NOT_RESET);
        assertThat(user.getLastLoginAt()).isNull();
        assertThat(user.getFailedLoginAttempts()).isEqualTo(3);
    }

    // ========== LOGIN FALLIDO ==========
    @Test
    @DisplayName("Registrar intento fallido de login")
    void shouldRecordFailedLogin() {
        UserIdentity user = createUser();
        int maxAttempts = 3;
        Duration lockDuration = Duration.ofMinutes(5);

        Outcome<UserIdentity> outcome = user.recordFailedLogin(now, maxAttempts, lockDuration);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_INVALID_CREDENTIALS);
        assertThat(user.getFailedLoginAttempts()).isEqualTo(1);
        assertThat(user.getLockedUntil()).isNull();
    }

    @Test
    @DisplayName("Bloquear usuario tras alcanzar máximo de intentos fallidos")
    void shouldLockUserAfterMaxFailedAttempts() {
        UserIdentity user = createUser();
        int maxAttempts = 3;
        Duration lockDuration = Duration.ofMinutes(5);

        user.recordFailedLogin(now, maxAttempts, lockDuration);
        user.recordFailedLogin(now, maxAttempts, lockDuration);
        Outcome<UserIdentity> outcome = user.recordFailedLogin(now, maxAttempts, lockDuration);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS);
        assertThat(user.getLockedUntil()).isNotNull();
        assertThat(user.getLockedUntil()).isAfter(now);
        assertThat(user.getLockedUntil()).isEqualTo(now.plus(lockDuration));
    }

    // ========== VERIFICACIÓN ==========
    @Test
    @DisplayName("Verificar usuario")
    void shouldVerify() {
        UserIdentity user = createUser();
        assertThat(user.isVerified()).isFalse();

        Outcome<UserIdentity> outcome = user.verify();

        assertThat(outcome.isSuccess()).isTrue();
        assertThat(user.isVerified()).isTrue();
    }

    @Test
    @DisplayName("Verificar usuario ya verificado falla")
    void shouldFailVerifyWhenAlreadyVerified() {
        UserIdentity user = createUser();
        user.verify();

        Outcome<UserIdentity> outcome = user.verify();

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_NOT_VERIFIED);
    }

    // ========== ACTUALIZACIÓN ==========
    @Test
    @DisplayName("Actualizar usuario con datos válidos")
    void shouldUpdate() {
        UserIdentity user = createUser();
        user.verify();

        UserIdentityName newName = UserIdentityName.of("newName");
        Email newEmail = Email.ofOrThrow("new@example.com");
        HashedPassword newPassword = HashedPassword.of("newHashed");

        Instant updateTime = now.plusSeconds(10);
        Outcome<UserIdentity> outcome = user.update(
                Optional.of(newName),
                Optional.of(newEmail),
                Optional.of(newPassword),
                updateTime
        );

        assertThat(outcome.isSuccess()).isTrue();
        assertThat(user.getName()).isEqualTo(newName);
        assertThat(user.getEmail()).isEqualTo(newEmail);
        assertThat(user.getHashedPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("Actualizar usuario no verificado falla")
    void shouldFailUpdateWhenNotVerified() {
        UserIdentity user = createUser();

        Instant updateTime = now.plusSeconds(10);
        Outcome<UserIdentity> outcome = user.update(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                updateTime
        );

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_NOT_VERIFIED);
    }

    // ========== SUSPENSIÓN ==========
    @Test
    @DisplayName("Suspender usuario activo")
    void shouldSuspend() {
        UserIdentity user = createUser();
        user.verify();

        Outcome<UserIdentity> outcome = user.suspend("Motivo de suspensión", now);

        assertThat(outcome.isSuccess()).isTrue();
        // ✅ CORREGIDO: comparar el valor interno
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.SUSPENDED);
    }

    @Test
    @DisplayName("Suspender usuario sin razón falla")
    void shouldFailSuspendWithoutReason() {
        UserIdentity user = createUser();
        user.verify();

        Outcome<UserIdentity> outcome = user.suspend(null, now);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_SUSPENSION_REQUIRES_REASON);
    }

    // ========== DESACTIVACIÓN ==========
    @Test
    @DisplayName("Desactivar usuario válido")
    void shouldDeactivate() {
        UserIdentity user = createUser();
        user.verify();

        when(deactivationPolicy.validate(user)).thenReturn(Outcome.ok());

        Outcome<UserIdentity> outcome = user.deactivate(deactivationPolicy, now, "Razón de desactivación");

        assertThat(outcome.isSuccess()).isTrue();
        // ✅ CORREGIDO: comparar el valor interno
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.INACTIVE);
        verify(deactivationPolicy).validate(user);
    }

    @Test
    @DisplayName("Desactivar usuario sin razón falla")
    void shouldFailDeactivateWithoutReason() {
        UserIdentity user = createUser();
        user.verify();

        Outcome<UserIdentity> outcome = user.deactivate(deactivationPolicy, now, null);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_DEACTIVATION_REASON_REQUIRED);
        verify(deactivationPolicy, never()).validate(any());
    }

    // ========== REACTIVACIÓN ==========
    @Test
    @DisplayName("Reactivar usuario inactivo")
    void shouldReactivate() {
        UserIdentity user = createUser();
        user.verify();
        // Desactivar primero
        when(deactivationPolicy.validate(user)).thenReturn(Outcome.ok());
        user.deactivate(deactivationPolicy, now, "Razón");

        // Reactivar
        Outcome<UserIdentity> outcome = user.reactivate(now);

        assertThat(outcome.isSuccess()).isTrue();
        // ✅ CORREGIDO: comparar el valor interno
        assertThat(user.getStatus().getValue()).isEqualTo(UserIdentityStatus.Status.ACTIVE);
        assertThat(user.getFailedLoginAttempts()).isZero();
        assertThat(user.getLockedUntil()).isNull();
    }

    @Test
    @DisplayName("Reactivar usuario no verificado falla con ERR_USER_NOT_VERIFIED")
    void shouldFailReactivateWhenNotVerified() {
        UserIdentity user = createUser(); // no verificado

        Outcome<UserIdentity> outcome = user.reactivate(now);

        assertThat(outcome.isFailure()).isTrue();
        // ✅ CORREGIDO: verificar el código de error correcto
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_NOT_VERIFIED);
    }

    // ========== canPerformSensitiveAction ==========
    @Test
    @DisplayName("Usuario puede realizar acción sensible si está verificado, activo y no bloqueado")
    void canPerformSensitiveAction_shouldSucceed() {
        UserIdentity user = createUser();
        user.verify();

        Outcome<UserIdentity> outcome = user.canPerformSensitiveAction(now);

        assertThat(outcome.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("Usuario no verificado no puede realizar acción sensible")
    void canPerformSensitiveAction_failWhenNotVerified() {
        UserIdentity user = createUser();

        Outcome<UserIdentity> outcome = user.canPerformSensitiveAction(now);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_NOT_VERIFIED);
    }

    @Test
    @DisplayName("Usuario bloqueado no puede realizar acción sensible")
    void canPerformSensitiveAction_failWhenLocked() {
        UserIdentity user = createUser();
        user.verify();
        
        int maxAttempts = 3;
        Duration lockDuration = Duration.ofMinutes(5);
        
        user.recordFailedLogin(now, maxAttempts, lockDuration);
        user.recordFailedLogin(now, maxAttempts, lockDuration);
        user.recordFailedLogin(now, maxAttempts, lockDuration); // bloqueado

        Outcome<UserIdentity> outcome = user.canPerformSensitiveAction(now);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_ACCOUNT_LOCKED);
    }

    @Test
    @DisplayName("Usuario inactivo no puede realizar acción sensible")
    void canPerformSensitiveAction_failWhenInactive() {
        UserIdentity user = createUser();
        user.verify();
        when(deactivationPolicy.validate(user)).thenReturn(Outcome.ok());
        user.deactivate(deactivationPolicy, now, "Razón");

        Outcome<UserIdentity> outcome = user.canPerformSensitiveAction(now);

        assertThat(outcome.isFailure()).isTrue();
        assertThat(outcome.getDetalles().get(0).getCode()).isEqualTo(UserIdentityError.ERR_USER_INACTIVE);
    }
}