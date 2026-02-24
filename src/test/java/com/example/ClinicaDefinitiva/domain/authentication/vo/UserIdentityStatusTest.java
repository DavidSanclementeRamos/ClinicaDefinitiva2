package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserIdentityStatusTest {

    @Test
    void shouldCreateActiveStatus() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertTrue(status.isActive());
        assertEquals("Activo", status.getDescription());
    }

    @Test
    void shouldTransitionFromPendingVerificationToActive() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.PENDING_VERIFICATION);
        assertTrue(status.canTransitionTo(UserIdentityStatus.Status.ACTIVE));

        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.ACTIVE);
        assertTrue(newStatus.isActive());
    }

    @Test
    void shouldNotTransitionFromPendingVerificationToSuspended() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.PENDING_VERIFICATION);
        assertFalse(status.canTransitionTo(UserIdentityStatus.Status.SUSPENDED));

        assertThrows(ValueObjectValidationException.class,
                () -> status.transitionTo(UserIdentityStatus.Status.SUSPENDED));
    }

    @Test
    void shouldTransitionFromActiveToInactive() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertTrue(status.canTransitionTo(UserIdentityStatus.Status.INACTIVE));

        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.INACTIVE);
        assertTrue(newStatus.isInactive());
    }

    @Test
    void shouldNotAllowTransitionToSameState() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertFalse(status.canTransitionTo(UserIdentityStatus.Status.ACTIVE));

        assertThrows(ValueObjectValidationException.class,
                () -> status.transitionTo(UserIdentityStatus.Status.ACTIVE));
    }
}