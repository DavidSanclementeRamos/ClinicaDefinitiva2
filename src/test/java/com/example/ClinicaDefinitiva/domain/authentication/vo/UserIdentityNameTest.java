package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class UserIdentityNameTest {

    // ========== Pruebas para create() ==========
    
    @Test
    @DisplayName("create() - nombre válido retorna Outcome exitoso")
    void create_shouldReturnSuccessForValidName() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("juan");
        assertThat(outcome.isSuccess()).isTrue();
        assertThat(outcome.getValue().get().getValue()).isEqualTo("juan");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("create() - nombre null o vacío retorna Outcome fallido")
    void create_shouldReturnFailureForNullOrEmpty(String invalidInput) {
        Outcome<UserIdentityName> outcome = UserIdentityName.create(invalidInput);
        assertThat(outcome.isFailure()).isTrue();
    }

    @Test
    @DisplayName("create() - nombre muy corto retorna Outcome fallido")
    void create_shouldReturnFailureForTooShort() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("ab");
        assertThat(outcome.isFailure()).isTrue();
    }

    @Test
    @DisplayName("create() - nombre muy largo retorna Outcome fallido")
    void create_shouldReturnFailureForTooLong() {
        Outcome<UserIdentityName> outcome = UserIdentityName.create("a".repeat(67));
        assertThat(outcome.isFailure()).isTrue();
    }

    // ========== Pruebas para of() ==========
    
    @Test
    @DisplayName("of() - nombre válido retorna instancia")
    void of_shouldReturnInstanceForValidName() {
        UserIdentityName name = UserIdentityName.of("juan");
        assertThat(name.getValue()).isEqualTo("juan");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("of() - nombre null o vacío lanza excepción")
    void of_shouldThrowForNullOrEmpty(String invalidInput) {
        assertThatThrownBy(() -> UserIdentityName.of(invalidInput))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("of() - nombre muy corto lanza excepción")
    void of_shouldThrowForTooShort() {
        assertThatThrownBy(() -> UserIdentityName.of("ab"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("of() - nombre muy largo lanza excepción")
    void of_shouldThrowForTooLong() {
        assertThatThrownBy(() -> UserIdentityName.of("a".repeat(67)))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
