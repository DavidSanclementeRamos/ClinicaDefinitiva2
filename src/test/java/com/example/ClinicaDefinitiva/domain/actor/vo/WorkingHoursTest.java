package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class WorkingHoursTest {

    @Test
    @DisplayName("Crear WorkingHours válido")
    void shouldCreateWorkingHours() {
        WorkingHours wh = WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 40);
        assertThat(wh.getStart()).isEqualTo(LocalTime.of(8, 0));
        assertThat(wh.getEnd()).isEqualTo(LocalTime.of(17, 0));
        assertThat(wh.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(wh.getDeclaredHoursPerWeek()).isEqualTo(40);
        assertThat(wh.duracionTotal().toHours()).isEqualTo(9);
    }

    @Test
    @DisplayName("Start después de End lanza excepción")
    void shouldThrowWhenStartAfterEnd() {
        assertThatThrownBy(() -> WorkingHours.of(LocalTime.of(17, 0), LocalTime.of(8, 0), DayOfWeek.MONDAY, 40))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Horas declaradas negativas lanza excepción")
    void shouldThrowForNegativeDeclaredHours() {
        assertThatThrownBy(() -> WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, -5))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Horas declaradas > 48 lanza excepción")
    void shouldThrowForExcessiveDeclaredHours() {
        assertThatThrownBy(() -> WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 50))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("isWithin verifica si una fecha cae dentro del horario")
    void testIsWithin() {
        WorkingHours wh = WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 40);
        LocalDateTime within = LocalDateTime.of(2025, 3, 10, 12, 0); // lunes
        assertThat(wh.isWithin(within)).isTrue();

        LocalDateTime outsideDay = LocalDateTime.of(2025, 3, 11, 12, 0); // martes
        assertThat(wh.isWithin(outsideDay)).isFalse();

        LocalDateTime beforeStart = LocalDateTime.of(2025, 3, 10, 7, 0);
        assertThat(wh.isWithin(beforeStart)).isFalse();

        LocalDateTime afterEnd = LocalDateTime.of(2025, 3, 10, 18, 0);
        assertThat(wh.isWithin(afterEnd)).isFalse();
    }

    @Test
    @DisplayName("isWithinRange verifica si un intervalo está dentro")
    void testIsWithinRange() {
        WorkingHours wh = WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 40);
        assertThat(wh.isWithinRange(LocalTime.of(9, 0), LocalTime.of(10, 0), DayOfWeek.MONDAY)).isTrue();
        assertThat(wh.isWithinRange(LocalTime.of(7, 0), LocalTime.of(9, 0), DayOfWeek.MONDAY)).isFalse();
        assertThat(wh.isWithinRange(LocalTime.of(10, 0), LocalTime.of(18, 0), DayOfWeek.MONDAY)).isFalse();
    }
}
