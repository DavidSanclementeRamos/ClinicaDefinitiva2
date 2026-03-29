package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    // ========== Pruebas para of() ==========

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user.name@domain.co", "a@b.c"})
    @DisplayName("of() - email válido retorna Outcome exitoso")
    void of_shouldReturnSuccessForValidEmail(String validEmail) {
        Outcome<Email> outcome = Email.of(validEmail);
        assertThat(outcome.isSuccess()).isTrue();
        assertThat(outcome.getValue().get().value()).isEqualTo(validEmail.trim().toLowerCase());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("of() - email null o vacío retorna Outcome fallido")
    void of_shouldReturnFailureForNullOrEmpty(String invalidEmail) {
        Outcome<Email> outcome = Email.of(invalidEmail);
        assertThat(outcome.isFailure()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "test@", "@domain", "test@domain", "test@domain."})
    @DisplayName("of() - email con formato inválido retorna Outcome fallido")
    void of_shouldReturnFailureForInvalidFormat(String invalidEmail) {
        Outcome<Email> outcome = Email.of(invalidEmail);
        assertThat(outcome.isFailure()).isTrue();
    }

    // ========== Pruebas para ofOrThrow() ==========

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user.name@domain.co"})
    @DisplayName("ofOrThrow() - email válido retorna instancia")
    void ofOrThrow_shouldReturnInstanceForValidEmail(String validEmail) {
        Email email = Email.ofOrThrow(validEmail);
        assertThat(email.value()).isEqualTo(validEmail.trim().toLowerCase());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("ofOrThrow() - email null o vacío lanza excepción")
    void ofOrThrow_shouldThrowForNullOrEmpty(String invalidEmail) {
        assertThatThrownBy(() -> Email.ofOrThrow(invalidEmail))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "test@", "@domain", "test@domain", "test@domain."})
    @DisplayName("ofOrThrow() - email con formato inválido lanza excepción")
    void ofOrThrow_shouldThrowForInvalidFormat(String invalidEmail) {
        assertThatThrownBy(() -> Email.ofOrThrow(invalidEmail))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
