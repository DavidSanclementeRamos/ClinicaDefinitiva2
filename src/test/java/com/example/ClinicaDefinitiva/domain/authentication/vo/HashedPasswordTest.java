package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class HashedPasswordTest {

    // ========== Pruebas para fromHash() ==========

    @ParameterizedTest
    @ValueSource(strings = {"$2a$10$encodedHash123", "hashed_password_123"})
    @DisplayName("fromHash() - hash válido retorna Outcome exitoso")
    void fromHash_shouldReturnSuccessForValidHash(String validHash) {
        Outcome<HashedPassword> outcome = HashedPassword.fromHash(validHash);
        assertThat(outcome.isSuccess()).isTrue();
        assertThat(outcome.getValue().get().getHash()).isEqualTo(validHash.trim());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("fromHash() - hash null o vacío retorna Outcome fallido")
    void fromHash_shouldReturnFailureForNullOrEmpty(String invalidHash) {
        Outcome<HashedPassword> outcome = HashedPassword.fromHash(invalidHash);
        assertThat(outcome.isFailure()).isTrue();
    }

    // ========== Pruebas para of() ==========

    @ParameterizedTest
    @ValueSource(strings = {"$2a$10$encodedHash123", "hashed_password_123"})
    @DisplayName("of() - hash válido retorna instancia")
    void of_shouldReturnInstanceForValidHash(String validHash) {
        HashedPassword hash = HashedPassword.of(validHash);
        assertThat(hash.getHash()).isEqualTo(validHash.trim());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("of() - hash null o vacío lanza excepción")
    void of_shouldThrowForNullOrEmpty(String invalidHash) {
        assertThatThrownBy(() -> HashedPassword.of(invalidHash))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}