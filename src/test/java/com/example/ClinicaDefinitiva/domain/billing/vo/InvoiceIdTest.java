package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InvoiceIdTest {

    @Test
    @DisplayName("Crear InvoiceId válido")
    void shouldCreateValidId() {
        InvoiceId id = InvoiceId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear InvoiceId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> InvoiceId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
