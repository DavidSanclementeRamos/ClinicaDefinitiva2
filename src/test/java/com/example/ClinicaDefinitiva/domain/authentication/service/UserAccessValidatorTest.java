package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.UserNotEligibleException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccessValidatorTest {

    @Mock
    private UserIdentityRepository userRepository;

    @InjectMocks
    private UserAccessValidator validator;

    private UserIdentity user;
    private UserIdentityId userId;
    private Instant now;
    private EntityContext context = EntityContext.USER_IDENTITY;

    @BeforeEach
    void setUp() {
        userId = UserIdentityId.from(1L);
        user = mock(UserIdentity.class);
        now = Instant.now();
        // No se necesita stubbing de user.getId() aquí
    }

    @Test
    void validateUserCanPerformSensitiveAction_shouldSucceed() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.canPerformSensitiveAction(now)).thenReturn(Outcome.ok(user));

        assertThatCode(() -> validator.validateUserCanPerformSensitiveAction(userId, now, context))
                .doesNotThrowAnyException();
    }

    @Test
    void validateUserCanPerformSensitiveAction_shouldThrowWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validator.validateUserCanPerformSensitiveAction(userId, now, context))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("El usuario no fue encontrado en el sistema");
    }

    @Test
    void validateUserCanPerformSensitiveAction_shouldThrowWhenUserNotVerified() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        OutcomeDetail detail = new OutcomeDetail(
                UserIdentityError.ERR_USER_NOT_VERIFIED,
                ErrorSeverity.ERROR,
                Category.TECNICO,
                EntityContext.USER_IDENTITY
        );
        Outcome<UserIdentity> failOutcome = Outcome.fail(detail);
        when(user.canPerformSensitiveAction(now)).thenReturn(failOutcome);

        assertThatThrownBy(() -> validator.validateUserCanPerformSensitiveAction(userId, now, context))
                .isInstanceOf(UserNotEligibleException.class);
    }

    @Test
    void validateUserCanPerformSensitiveAction_shouldThrowWhenAccountLocked() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        OutcomeDetail detail = new OutcomeDetail(
                UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                ErrorSeverity.ERROR,
                Category.TECNICO,
                EntityContext.USER_IDENTITY
        );
        Outcome<UserIdentity> failOutcome = Outcome.fail(detail);
        when(user.canPerformSensitiveAction(now)).thenReturn(failOutcome);

        assertThatThrownBy(() -> validator.validateUserCanPerformSensitiveAction(userId, now, context))
                .isInstanceOf(UserNotEligibleException.class);
    }

    @Test
    void validateUserCanPerformSensitiveAction_shouldThrowWhenInactive() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        OutcomeDetail detail = new OutcomeDetail(
                UserIdentityError.ERR_USER_INACTIVE,
                ErrorSeverity.ERROR,
                Category.TECNICO,
                EntityContext.USER_IDENTITY
        );
        Outcome<UserIdentity> failOutcome = Outcome.fail(detail);
        when(user.canPerformSensitiveAction(now)).thenReturn(failOutcome);

        assertThatThrownBy(() -> validator.validateUserCanPerformSensitiveAction(userId, now, context))
                .isInstanceOf(UserNotEligibleException.class);
    }
}