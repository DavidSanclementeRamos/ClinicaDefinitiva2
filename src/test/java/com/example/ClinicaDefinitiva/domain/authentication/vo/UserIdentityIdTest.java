package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserIdentityIdTest {

    @Test
    void shouldCreateUserIdentityIdWhenValueIsValid() {
        UserIdentityId id = UserIdentityId.from(123L);
        assertEquals(123L, id.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(ValueObjectValidationException.class, () -> UserIdentityId.from(null));
    }
}

