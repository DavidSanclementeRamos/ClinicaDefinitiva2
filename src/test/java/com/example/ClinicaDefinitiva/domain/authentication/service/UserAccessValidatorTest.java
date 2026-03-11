package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.application.exceptions.authentication.UserIdentityNoFoundException;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.UserNotEligibleException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccessValidatorTest {

    private UserIdentityRepository userRepo;
    private UserAccessValidator validator;
    private UserIdentityId userId;
    private EntityContext context;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserIdentityRepository.class);
        validator = new UserAccessValidator(userRepo);
        userId = new UserIdentityId(1L);
        context = EntityContext.USER_IDENTITY;
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

       // assertThrows(UserIdentityNoFoundException.class,
              //  () -> validator.validateUserCanPerformSensitiveAction(userId, Instant.now(), context));
    }

    @Test
    void shouldPassWhenUserIsEligible() {
        UserIdentity user = mock(UserIdentity.class);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(user.canPerformSensitiveAction(any())).thenReturn(Outcome.ok(user));

        assertDoesNotThrow(() -> validator.validateUserCanPerformSensitiveAction(userId, Instant.now(), context));
    }
    @Test
    void shouldThrowBusinessExceptionWhenUserNotVerified() {
        UserIdentity user = mock(UserIdentity.class);
        OutcomeDetail detail = new OutcomeDetail(
                UserIdentityError.ERR_USER_NOT_VERIFIED,
                ErrorSeverity.ERROR,
                Category.TECNICO,
                context
        );
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(user.canPerformSensitiveAction(any())).thenReturn(Outcome.fail(List.of(detail)));

        UserNotEligibleException ex = assertThrows(UserNotEligibleException.class,
                () -> validator.validateUserCanPerformSensitiveAction(userId, Instant.now(), context));

        // Verificar que el código de error está en los detalles
        assertEquals(UserIdentityError.ERR_USER_NOT_VERIFIED, ex.getDetails().get(0).getCode());
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserLocked() {
        UserIdentity user = mock(UserIdentity.class);
        OutcomeDetail detail = new OutcomeDetail(
                UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                ErrorSeverity.ERROR,
                Category.TECNICO,
                context
        );
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(user.canPerformSensitiveAction(any())).thenReturn(Outcome.fail(List.of(detail)));

        UserNotEligibleException ex = assertThrows(UserNotEligibleException.class,
                () -> validator.validateUserCanPerformSensitiveAction(userId, Instant.now(), context));

        // Verificar que el código de error está en los detalles
        assertEquals(UserIdentityError.ERR_USER_ACCOUNT_LOCKED, ex.getDetails().get(0).getCode());
    }
}