package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AestheticDetailsTest {

    @Test
    @DisplayName("Crear detalles estéticos válidos")
    void shouldCreateValid() {
        AestheticDetails details = new AestheticDetails(
                "WHITENING", "Peróxido de hidrógeno", "Dientes más blancos en 3 tonos"
        );
        assertThat(details.getAestheticType()).isEqualTo("WHITENING");
        assertThat(details.getMaterialUsed()).isEqualTo("Peróxido de hidrógeno");
        assertThat(details.getExpectedResult()).isEqualTo("Dientes más blancos en 3 tonos");
    }

    @Test
    @DisplayName("Tipo estético obligatorio")
    void shouldRequireAestheticType() {
        assertThatThrownBy(() -> new AestheticDetails(null, "Material", "Resultado"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Tipo estético debe tener al menos 3 caracteres")
    void shouldEnforceMinTypeLength() {
        assertThatThrownBy(() -> new AestheticDetails("AB", "Material", "Resultado largo"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Tipo estético debe ser válido (catálogo)")
    void shouldValidateAestheticType() {
        assertThatThrownBy(() -> new AestheticDetails("INVALID", "Material", "Resultado largo"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Resultado esperado debe tener al menos 10 caracteres")
    void shouldEnforceMinResultLength() {
        assertThatThrownBy(() -> new AestheticDetails("WHITENING", "Material", "Corto"))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
