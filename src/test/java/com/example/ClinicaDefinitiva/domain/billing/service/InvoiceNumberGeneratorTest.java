package com.example.ClinicaDefinitiva.domain.billing.service;

import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumber;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InvoiceNumberGeneratorTest {

    private InvoiceNumberGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new InvoiceNumberGenerator("FAC", 0);
    }

    @Test
    @DisplayName("Generar primer número de factura")
    void shouldGenerateFirstNumber() {
        InvoiceNumber number = generator.next();
        assertThat(number.getValue()).isEqualTo("FAC-0001");
    }

    @Test
    @DisplayName("Generar secuencia incremental")
    void shouldGenerateIncrementalNumbers() {
        InvoiceNumber first = generator.next();
        assertThat(first.getValue()).isEqualTo("FAC-0001");

        InvoiceNumber second = generator.next();
        assertThat(second.getValue()).isEqualTo("FAC-0002");

        InvoiceNumber third = generator.next();
        assertThat(third.getValue()).isEqualTo("FAC-0003");
    }

    @Test
    @DisplayName("Obtener número actual sin avanzar secuencia")
    void shouldGetCurrentWithoutAdvancing() {
        InvoiceNumber current = generator.current();
        assertThat(current.getValue()).isEqualTo("FAC-0000");

        generator.next();
        InvoiceNumber afterFirst = generator.current();
        assertThat(afterFirst.getValue()).isEqualTo("FAC-0001");
    }

    @Test
    @DisplayName("Reiniciar secuencia")
    void shouldResetSequence() {
        generator.next(); // 1
        generator.next(); // 2
        generator.reset(10);
        assertThat(generator.current().getSequence()).isEqualTo(10);
        InvoiceNumber next = generator.next();
        assertThat(next.getValue()).isEqualTo("FAC-0011");
    }

    @Test
    @DisplayName("Crear generador con prefijo inválido lanza excepción")
    void shouldThrowForInvalidPrefix() {
        assertThatThrownBy(() -> new InvoiceNumberGenerator("", 0))
                .isInstanceOf(ValueObjectValidationException.class);
        assertThatThrownBy(() -> new InvoiceNumberGenerator(null, 0))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Crear generador con secuencia inicial negativa lanza excepción")
    void shouldThrowForNegativeInitialSequence() {
        assertThatThrownBy(() -> new InvoiceNumberGenerator("FAC", -1))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Reiniciar con valor negativo lanza excepción")
    void shouldThrowForNegativeReset() {
        assertThatThrownBy(() -> generator.reset(-1))
                .isInstanceOf(ValueObjectValidationException.class);
    }
}
