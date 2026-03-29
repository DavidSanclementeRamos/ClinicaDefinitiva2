package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ServiceDurationTest {

    @Test
    @DisplayName("Crear duración en minutos válida")
    void shouldCreateFromMinutes() {
        ServiceDuration duration = ServiceDuration.of(30);
        assertThat(duration.getMinutes()).isEqualTo(30);
        assertThat(duration.toReadableFormat()).isEqualTo("30m");
    }

    @Test
    @DisplayName("Crear duración desde horas")
    void shouldCreateFromHours() {
        ServiceDuration duration = ServiceDuration.ofHours(2);
        assertThat(duration.getMinutes()).isEqualTo(120);
        assertThat(duration.toReadableFormat()).isEqualTo("2h");
    }

    @Test
    @DisplayName("Duración mínima 15 minutos")
    void shouldEnforceMinimum() {
        assertThatThrownBy(() -> ServiceDuration.of(10))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Duración máxima 480 minutos (8 horas)")
    void shouldEnforceMaximum() {
        assertThatThrownBy(() -> ServiceDuration.of(500))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Calcular duración entre fechas")
    void shouldCreateBetween() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 9, 0);
        LocalDateTime end = start.plusMinutes(90);
        ServiceDuration duration = ServiceDuration.between(start, end);
        assertThat(duration.getMinutes()).isEqualTo(90);
    }

    @Test
    @DisplayName("Operaciones aritméticas")
    void shouldSupportArithmetic() {
        ServiceDuration d1 = ServiceDuration.of(30);
        ServiceDuration d2 = ServiceDuration.of(45);
        assertThat(d1.plus(d2).getMinutes()).isEqualTo(75);
        assertThat(d2.minus(d1).getMinutes()).isEqualTo(15);
        assertThatThrownBy(() -> d1.minus(d2)).isInstanceOf(ValueObjectValidationException.class);
        assertThat(d1.multiply(3).getMinutes()).isEqualTo(90);
    }

    @Test
    @DisplayName("Métodos de consulta")
    void testQueries() {
        ServiceDuration shortDur = ServiceDuration.of(25);
        ServiceDuration longDur = ServiceDuration.of(150);
        assertThat(shortDur.isShort()).isTrue();
        assertThat(longDur.isLong()).isTrue();
        assertThat(shortDur.isStandardSlot()).isFalse(); // 25 no es múltiplo de 15
        assertThat(ServiceDuration.of(45).isStandardSlot()).isTrue();
        assertThat(longDur.isLongerThan(shortDur)).isTrue();
        assertThat(shortDur.isShorterThan(longDur)).isTrue();
    }
}
