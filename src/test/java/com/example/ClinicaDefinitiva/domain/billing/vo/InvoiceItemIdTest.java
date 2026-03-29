package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InvoiceItemIdTest {

    @Test
    @DisplayName("Crear InvoiceItemId válido")
    void shouldCreateValidId() {
        InvoiceItemId id = InvoiceItemId.of(1L);
        assertThat(id.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Crear InvoiceItemId con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> InvoiceItemId.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
