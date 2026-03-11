package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization.AuthorizationVoError;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class HashedPasswordTest {

    @Test
    void shouldCreateValidHashedPassword() {
        Outcome<HashedPassword> outcome = HashedPassword.fromHash("$2a$10$abcdefghijklmnopqrstuv");
        assertTrue(outcome.isSuccess());
        assertEquals("$2a$10$abcdefghijklmnopqrstuv", outcome.getValue().get().getHash());
        assertTrue(outcome.getDetalles().isEmpty());
    }

    @Test
    void shouldFailWhenHashIsNull() {
        Outcome<HashedPassword> outcome = HashedPassword.fromHash(null);
        assertTrue(outcome.isFailure());

        List<OutcomeDetail> detalles = outcome.getDetalles();
        assertEquals(1, detalles.size());
       // assertEquals(AuthorizationVoError.ERR_USER_PASSWORD_HASH_NULL, detalles.get(0).getCode());
        assertEquals(ErrorSeverity.ERROR, detalles.get(0).getSeverity());
    }

    @Test
    void shouldFailWhenHashIsEmpty() {
        Outcome<HashedPassword> outcome = HashedPassword.fromHash("   ");
        assertTrue(outcome.isFailure());

        List<OutcomeDetail> detalles = outcome.getDetalles();
        assertEquals(1, detalles.size());
        //assertEquals(VoAccesError.ERR_USER_PASSWORD_HASH_EMPTY, detalles.get(0).getCode());
    }
}
