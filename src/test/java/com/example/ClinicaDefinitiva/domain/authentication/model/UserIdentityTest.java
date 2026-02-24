package com.example.ClinicaDefinitiva.domain.authentication.model;

import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

class UserIdentityTest {

    private Email email =  Email.of("test@example.com").getValue().get();
    private HashedPassword password = HashedPassword.fromHash("$2a$10$abcdefghijklmnopqrstuv").getValue().get();
    private UserIdentityName name = UserIdentityName.create("David").getValue().get();

    @Test
    void shouldRegisterNewUser() {
        Instant now = Instant.now();
        UserIdentity user = UserIdentity.register(email, password, name, now);

        assertEquals(email, user.getEmail());
        assertEquals(password, user.getHashedPassword());
        assertEquals(name, user.getName());
        assertEquals(UserIdentityStatus.Status.ACTIVE, user.getStatus().getValue());
        assertFalse(user.isVerified());
    }

    @Test
    void shouldRecordSuccessfulLoginWhenNotLocked() {
        Instant now = Instant.now();
        UserIdentity user = UserIdentity.register(email, password, name, now);

        Outcome<UserIdentity> outcome = user.recordSuccessfulLogin(now.plusSeconds(60));
        assertTrue(outcome.isSuccess());
        assertEquals(0, user.getFailedLoginAttempts());
        assertEquals(now.plusSeconds(60), user.getLastLoginAt());
    }

    @Test
    void shouldFailLoginWhenLocked() {
        Instant now = Instant.now();
        UserIdentity user = UserIdentity.register(email, password, name, now);

        // Simula bloqueo
        user.recordFailedLogin(now, 1, Duration.ofMinutes(5));

        Outcome<UserIdentity> outcome = user.recordSuccessfulLogin(now.plusSeconds(60));
        assertTrue(outcome.isFailure());
        assertEquals(UserIdentityError.ERR_USER_FAILED_ATTEMPTS_NOT_RESET, outcome.getDetalles().get(0).getCode());
    }

    @Test
    void shouldVerifyUser() {
        Instant now = Instant.now();
        UserIdentity user = UserIdentity.register(email, password, name, now);

        Outcome<UserIdentity> outcome = user.verify();
        assertTrue(outcome.isSuccess());
        assertTrue(user.isVerified());
    }

    @Test
    void shouldFailSensitiveActionIfNotVerified() {
        Instant now = Instant.now();
        UserIdentity user = UserIdentity.register(email, password, name, now);

        Outcome<UserIdentity> outcome = user.canPerformSensitiveAction(now);
        assertTrue(outcome.isFailure());
        assertEquals(UserIdentityError.ERR_USER_NOT_VERIFIED, outcome.getDetalles().get(0).getCode());
    }
}
