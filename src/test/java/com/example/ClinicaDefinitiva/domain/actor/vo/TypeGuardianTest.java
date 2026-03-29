package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class TypeGuardianTest {

    @ParameterizedTest
    @CsvSource({
            "MAMA, Madre",
            "PAPA, Padre",
            "HERMANO, Hermano",
            "HERMANA, Hermana",
            "ABUELO, Abuelo",
            "ABUELA, Abuela",
            "TIO, Tío",
            "TIA, Tía",
            "PRIMO, Primo",
            "PRIMA, Prima",
            "TUTOR_LEGAL, Tutor Legal",
            "OTRO, Otro"
    })
    @DisplayName("Crear TypeGuardian con código y descripción válidos")
    void shouldCreateValidTypeGuardian(String code, String description) {
        TypeGuardian type = TypeGuardian.of(code, description);
        assertThat(type.getCode()).isEqualTo(code);
        assertThat(type.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("Crear TypeGuardian con código inválido lanza excepción")
    void shouldThrowForInvalidCode() {
        assertThatThrownBy(() -> TypeGuardian.of("INVALID", "Descripción"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear TypeGuardian con descripción vacía lanza excepción")
    void shouldThrowForEmptyDescription() {
        assertThatThrownBy(() -> TypeGuardian.of("MAMA", "   "))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("fromCode reconstruye con descripción por defecto")
    void testFromCode() {
        TypeGuardian type = TypeGuardian.fromCode("MAMA");
        assertThat(type.getCode()).isEqualTo("MAMA");
        assertThat(type.getDescription()).isEqualTo("Madre");
    }

    @Test
    @DisplayName("Métodos semánticos funcionan")
    void testSemanticMethods() {
        TypeGuardian mama = TypeGuardian.fromCode("MAMA");
        assertThat(mama.isParent()).isTrue();
        assertThat(mama.isGrandparent()).isFalse();
        assertThat(mama.isSibling()).isFalse();
        assertThat(mama.isLegalGuardian()).isFalse();
        assertThat(mama.isDirectFamily()).isTrue();
        assertThat(mama.getLegalPriority()).isEqualTo(1);

        TypeGuardian tutor = TypeGuardian.fromCode("TUTOR_LEGAL");
        assertThat(tutor.isLegalGuardian()).isTrue();
        assertThat(tutor.getLegalPriority()).isEqualTo(2);
    }
}