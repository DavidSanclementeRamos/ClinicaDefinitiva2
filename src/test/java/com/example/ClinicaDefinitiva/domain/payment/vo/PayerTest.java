package com.example.ClinicaDefinitiva.domain.payment.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PayerTest {

    @Test
    @DisplayName("Crear pagador paciente")
    void createPatient() {
        Payer payer = Payer.patient("Juan Pérez");
        assertThat(payer.isPatient()).isTrue();
        assertThat(payer.isInstitutional()).isFalse();
        assertThat(payer.getName()).isEqualTo("Juan Pérez");
        assertThat(payer.getIdentifier()).isNull();
    }

    @Test
    @DisplayName("Crear pagador EPS con NIT")
    void createEps() {
        Payer payer = Payer.eps("Sura", "123456789-0");
        assertThat(payer.isEPS()).isTrue();
        assertThat(payer.isInstitutional()).isTrue();
        assertThat(payer.getIdentifier()).isEqualTo("123456789-0");
    }

    @Test
    @DisplayName("Crear pagador con nombre vacío lanza excepción")
    void emptyName_throws() {
        assertThatThrownBy(() -> Payer.patient(""))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> Payer.eps("", "123"))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Tipo nulo lanza excepción")
    void nullType_throws() {
        assertThatThrownBy(() -> Payer.of(null, "id", "name"))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
