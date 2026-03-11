package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class UserIdentityNameTest {

    @Test
    void shouldCreateValidName() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("  David  ");
        assertTrue(outcome.isSuccess());
        assertEquals("David", outcome.getValue().get().getValue());
        assertTrue(outcome.getDetalles().isEmpty());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create(null);
        assertTrue(outcome.isFailure());

        List<OutcomeDetail> detalles = outcome.getDetalles();
        assertEquals(1, detalles.size());
      //  assertEquals(VoAccesError.ERR_USER_NAME_NULL, detalles.get(0).getCode());
        assertEquals(ErrorSeverity.ERROR, detalles.get(0).getSeverity());
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("   ");
        assertTrue(outcome.isFailure());

        List<OutcomeDetail> detalles = outcome.getDetalles();
        assertEquals(1, detalles.size());
      // assertEquals(VoAccesError.ERR_USER_NAME_EMPTY, detalles.get(0).getCode());
    }

    @Test
    void shouldFailWhenNameTooShort() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("Al");
        assertTrue(outcome.isFailure());

        List<OutcomeDetail> detalles = outcome.getDetalles();
       // assertEquals(VoAccesError.ERR_USER_NAME_TOO_SHORT, detalles.get(0).getCode());
    }

    @Test
    void shouldFailWhenNameTooLong() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("abcdefghijklmnop");
        assertTrue(outcome.isFailure());

        List<OutcomeDetail> detalles = outcome.getDetalles();
       // assertEquals(VoAccesError.ERR_USER_NAME_TOO_LONG, detalles.get(0).getCode());
    }
}